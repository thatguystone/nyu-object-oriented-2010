package translator;

import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.HashSet;

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

	/**
	 * List of all fields in this class
	 * Field name -> Field object
	 */
	private HashSet<JavaClassField> fields = new HashSet<JavaClassField>();

	/**
	 * List of all methods in this class
	 * Method name -> Method object
	 */
	//private HashSet<JavaMethod> methods = new HashSet<JavaMethod>();
	
	JavaClass(JavaFile file, String pkg, Node n) {
		this.file = file;
		this.pkg = pkg;
		this.name = (String)n.get(1);
		this.setup(n);
		
		//and register ourself with JavaPackages
		JavaStatic.pkgs.addClass(this);
	}
	
	public void addField(JavaClassField field) {
		this.fields.add(field);
	}

	protected void process() {
		//go for a nice visit to everyone
		//this.dispatch(this.node);
		for (Object o : this.node) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
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

	public void visitFieldDeclaration(GNode n) {
		//cannot store the field since a single field declaration
		//might declare multiple fields
		JavaFieldDec fieldDec = new JavaFieldDec(this, (Node)n);
	}
	
	public void visit(Node n) {
		//We don't want to iterate through the class's entire subtree, just its
		//immediate children, xtc doesn't differentiate between class field declarations
		//and method field declarations 
		/*
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
		*/
	}
}
