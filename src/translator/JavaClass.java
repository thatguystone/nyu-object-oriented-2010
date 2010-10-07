package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

class JavaClass extends Visitor {
	/**
	 * The package this class is contained in.
	 */
	private String pkg;
	
	/**
	 * The name of the class.
	 */
	private String name;
	
	/****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ***   AT SOME POINT, WE'RE GOING TO NEED A LIST OF ALL IMPORTED PACKAGES FOR METHOD / PROPERTY   ***
	 ***                                         RESOLVING                                            ***
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************/
	
	JavaClass(String pkg, GNode n) {
		this.pkg = pkg;
		this.name = (String)n.get(1);
	}
	
	public void process(Node n) {
		//see if we are doing any inheritance -- if we aren't, then we need to import
		//our base object
		if (n.get(3) == null) {
			if (!JavaStatic.javaFiles.classExists("java.lang.Object"))
				JavaFile.importPkg("java.lang.Object");
		}
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
		
		System.out.println(parent);
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
