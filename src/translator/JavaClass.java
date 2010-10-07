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
	
	/****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ***   AT SOME POINT, WE'RE GOING TO NEED A LIST OF ALL IMPORTED PACKAGES FOR METHOD / PROPERTY   ***
	 ***                                         RESOLVING                                            ***
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************/
	
	JavaClass(JavaFile file, String pkg, Node n) {
		this.file = file;
		this.pkg = pkg;
		this.name = (String)n.get(1);
		this.setup(n);
		
		//and register ourself with JavaPackages
		JavaStatic.pkgs.addClass(this);
	}
	
	protected void process() {
		//see if we are doing any inheritance -- if we aren't, then we need to import
		//our base object
		if (this.node.get(3) == null)
			JavaPackages.importFile("java.lang.Object");
		
		//and visit the rest
		this.dispatch(this.node);
	}
	
	/**
	 * Returns the name and package of the class in the java.lang.Pkg form.
	 */
	public String getName() {
		return this.pkg + "." + this.name;
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
		
		//need default imports / imports before we can do inheritance
		System.out.println("Found inheritance: " + parent + "...but where does it come from?!");
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
