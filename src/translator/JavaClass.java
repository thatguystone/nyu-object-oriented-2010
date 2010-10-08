package translator;

import xtc.tree.GNode;
import xtc.tree.Node;

class JavaClass extends ActivatableVisitor implements Nameable {
	/**
	 * The package this class is contained in.
	 */
	private String pkg;
	
	/**
	 * The name of the class.
	 */
	private String name;
	
	/**
	 * The name of the file that contains this class.
	 */
	private JavaFile file;
	
	JavaClass(JavaFile file, String pkg, Node n) {
		this.file = file;
		this.pkg = pkg;
		this.name = (String)n.get(1);
		this.setup(n);
		
		//and register ourself with JavaPackages
		JavaStatic.pkgs.addClass(this);
	}
	
	protected void process() {
		//go for a nice visit to everyone
		this.dispatch(this.node);
	}
	
	/**
	 * Returns the name and package of the class in the java.lang.Pkg form.
	 */
	public String getName() {
		return this.getName(true);
	}
	
	public String getName(boolean withPackage) {
		return (withPackage ? this.pkg + "." : "") + this.name;
	}
	
	public String getPackageName() {
		return this.pkg;
	}
	
	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	 
	/**
	 * Handles resolving dependencies for inheritance.
	 */
	public void visitExtension(GNode n) {
		/** 
		 * get the name of the class we inherit from, as:
		 *	Extension(
		 *		(0) - Type(
		 *			(0) - QualifiedIdentifier(
		 *				(0) - "Object"
		 *			),
		 *			null
		 *		)
		 *	)
		 */
		//java only supports single inheritance...no need for loops or anything here
		String parent = (String)((GNode)((GNode)n.get(0)).get(0)).get(0);
		
		//with the extension, we need to activate it (ie. process it) before we can use it
		this.file.getImport(parent).activate();
	}
	
	/*public void visitMethodDeclaration(GNode n) {
		methods.add("nameOfMethod", new JavaMethod(n));
	}*/
	
	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}
