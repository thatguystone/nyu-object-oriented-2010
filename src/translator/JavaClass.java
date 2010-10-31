package translator;

import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;

class JavaClass extends ActivatableVisitor implements Nameable {
	/**
	 * The name of the class (not fully qualified, just the name).
	 */
	private String name;
	
	/**
	 * The parent class.  This is going to be something from "extends" or java.lang.Object.
	 */
	private JavaClass parent;
	
	/**
	 * List of all virtual methods in this class (v is for virtual).
	 * Method name -> Method object
	 */
	private LinkedHashMap<String, JavaMethod> vMethods = new LinkedHashMap<String, JavaMethod>();
	
	/**
	 * List of all non-virtual methods in this class (p is for private).
	 * Method name -> Method object
	 */
	private LinkedHashMap<String, JavaMethod> pMethods = new LinkedHashMap<String, JavaMethod>();
	
	/**
	 * List of all the methods from the parent class that haven't been overriden(written?) (i is for inherited).
	 * Points to a {@link JavaClass} because we're going to need the class for vtable resolution.
	 */
	private LinkedHashMap<String, JavaClass> vTable = new LinkedHashMap<String, JavaClass>();

	/**
	 * List of all methods WITHOUT their signature
	 * I'm adding this because we don't need overloading atm
	 * and this is much easier than having a method call
	 figure out the type of its arguments.
	 * Method name -> Method name with sig
	 */
	private Hashtable<String, String> methods = new Hashtable<String, String>();
	
	/**
	 * SAEKJFA;WIE JF K;LSDFJ ASILD JFASD;IFJ!!!!!!! WHY DOES JAVA NOT INHERIT CONSTRUCTORS?!?!?!?!?!?!?!?!?!??!
	 * This feels so dirty and wrong.
	 */
	JavaClass(GNode n) {
		super(n);
	}
	JavaClass(JavaScope scope, GNode n) {
		super(scope, n);
	}
	
	/**
	 * There are a few minor details we need to sort out once we can access our GNode.
	 */
	protected void onNodeSetup() {
		this.setupVisibility((GNode)this.node.get(0));
	}
	
	/**
	 * Only to be used upon activation.  Does everything we need to get this class ready for translation.
	 */
	protected void process() {
		//go for a nice visit to see everyone
		this.dispatch(this.node);
		
		//activate the parent file so that all the classes with him are included
		this.getFile().activate();
		
		if (JavaStatic.runtime.test("debug"))
			System.out.println("Class activated: " + this.getName());
		
		//check if we have a parent; if we don't, then java.lang.Object is our parent
		this.setParent("java.lang.Object");
		
		/*
		System.out.println("Setting up vtable for " + this.getName());
		
		for (StackTraceElement e : Thread.currentThread().getStackTrace())
			System.out.println(e.toString());
		
		System.out.println();
		System.out.println();
		//*/
		
		//once we're sure we have a parent, then add all our inherited methods
		//and, once the VTable is setup, go ahead and activate the methods
		this.setupVTable();
	}

	/**
	 * Gets the method from its signature.
	 */
	public JavaMethod getMethod(String sig) {
		if (this.vMethods.containsKey(sig))
			return this.vMethods.get(sig);
		
		if (this.pMethods.containsKey(sig))
			return this.pMethods.get(sig);
		
		return null;
	}

	/**
	 * Setup our parent.  Can only be run once, then everything is permanent.
	 */
	private void setParent(String parent) {
		//java.lang.Object has no parent
		if (this.getName(true).equals("java.lang.Object"))
			return;
		
		//only allow one parent to be set
		if (this.parent == null) {
			if (JavaStatic.runtime.test("debug"))
				System.out.println(this.getName() + " extends " + this.getFile().getImport(parent).getName());
			
			//set our parent from its name in import
			this.parent = this.getFile().getImport(parent);
			
			//with the extension, we need to activate it (ie. process it) before we can use it
			this.parent.activate();
		}
	}

	/**
	 * Go through all the parents and get their virtual methods, then just add them.  To do this, we test
	 * if we first have a parent (java.lang.Object doesn't); if we have a parent, grab his virtual methods,
	 * see if we override them (they'll be set in either this.pMethods or this.vMethods), if we don't, then add
	 * them, otherwise, ignore.
	 * 
	 * Since we know that there are problems with VTable resolution from parents, we're going to first add our methods
	 * to our vtable and grab our parents, then we're going to activate our methods so that they translate.
	 */
	private void setupVTable() {

	}
	
	/**
	 * ==================================================================================================
	 * Nameable Methods
	 */
	
	/**
	 * Gets the fully qualified java name.
	 */
	public String getName() {
		return this.getName(true);
	}
	
	/**
	 * Gets the java name.
	 *
	 * @param fullName True for the fully-qualified java name; false for just the last part of the name.
	 */
	public String getName(boolean fullName) {
		String name = "";
		if (fullName)
			name += this.getPackageName() + ".";
		
		return name + this.name; 
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	 
	/**
	 * Handles resolving dependencies for inheritance.  When it sees an extension, it throws the name
	 * to its parent file's import manager in order to resolve the name and activate the class so that 
	 * it can extend it properly.
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
		this.setParent((String)((GNode)((GNode)n.get(0)).get(0)).get(0));
	}
	
	/**
	 * Take in a method.  Adds the method to our method table with its signature.
	 */
	public void visitMethodDeclaration(GNode n) {
	}
	
	public void visitConstructorDeclaration(GNode n) {
	}
}
