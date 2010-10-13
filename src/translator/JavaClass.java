package translator;

import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;

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
	 * The parent class.  This is going to be something from "extends" or java.lang.Object.
	 */
	private JavaClass parent;

	/**
	 * List of all fields in this class
	 * Field name -> Field object
	 */
	private Hashtable<String, JavaField> fields = new Hashtable<String, JavaField>();

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
	 * Builds a class from a node.
	 *
	 * @param file The file that contains this class (the "parent file").
	 * @param pkg The name of the package this class is contained in.
	 * @param n The node that contains the defintion for this class.
	 */
	JavaClass(JavaFile file, String pkg, Node n) {
		this.file = file;
		this.pkg = pkg;
		this.name = (String)n.get(1);
		this.setup(n);
		
		//and register ourself with JavaPackages
		JavaStatic.pkgs.addClass(this);

		this.addType();
	}			

	/**
	 * Only to be used upon activation.  Does everything we need to get this class ready for translation.
	 */
	protected void process() {
		//go for a nice visit to see everyone
		this.dispatch(this.node);
		
		if (JavaStatic.runtime.test("debug"))
			System.out.println("Class activated: " + this.getName());
		
		//check if we have a parent; if we don't, then java.lang.Object is our parent
		this.setParent("java.lang.Object");
		
		//once we're sure we have a parent, then add all our inherited methods
		this.setupVTable();
	}	

	//Might get removed
	/**
	public boolean hasID(String ID) {
		if (this.hasField(ID)) return true;
		if (this.hasMethod(ID)) return true;
		return false;
	}

	/**
	 * =================================================================================================
	 * Some reorganizing and commenting needed
	 *

	//The field methods can be used by any block
	//They aren't in JavaScope because not every JavaScope object is
	//a block.
	//TODO create a JavaBlock or whatever class to handle some/all of these methods

	public boolean hasField(String ID) {
		if (this.fields.containsKey(ID)) {
			this.lastID = ID;
			this.lastScope = this.fields.get(ID);
			return true;
		}
		return false;
	}

	public boolean hasMethod(String ID) {
		if (this.hasPMethod(ID)) return true;
		if (this.hasVMethod(ID)) return true;
		if (this.hasIMethod(ID)) return true;
		return false;
	}

	public boolean hasPMethod(String ID) {
		if (this.pMethods.containsKey(ID)) {
			this.lastID = ID;
			this.lastScope = this.getPMethod(ID);
			return true;
		}
		return false;
	}

	public boolean hasVMethod(String ID) {
		if (this.vMethods.containsKey(ID)) {
			this.lastID = ID;
			this.lastScope = this.getVMethod(ID);
			return true;
		}
		return false;
	}

	public boolean hasIMethod(String ID) {
		if (this.iMethods.containsKey(ID)) {
			this.lastID = ID;
			this.lastScope = this.getIMethod(ID);
			return true;
		}
		return false;
	}

	public JavaScope getScopeFromID(String ID) {
		//at the moment this should always work
		if (lastID == ID) return lastScope;
		//TODO get a scope when lastID wasn't set and throw exception if no scope exists
		return null;
	}

	public JavaMethod getMethod(String method) {
		if (this.hasPMethod(method)) return this.getPMethod(method);
		if (this.hasVMethod(method)) return this.getVMethod(method);
		if (this.hasIMethod(method)) return this.getIMethod(method);
		return null;
	}

	public JavaMethod getPMethod(String method) {
		return this.pMethods.get(method);
	}

	public JavaMethod getVMethod(String method) {
		return this.vMethods.get(method);
	}

	public JavaMethod getIMethod(String method) {
		return this.iMethods.get(method).getMethod(method);
	}

	/**
	 * End
	 * =================================================================================================
	 */

	/**
	 * Returns the name and package of the class in the java.lang.Pkg form.
	 */
	public String getName() {
		return this.getName(true);
	}
	
	/**
	 * Returns the name of the package, as specified.
	 *
	 * @param withPackage If true, includes the package name in the name, as in "java.lang.Object"; otherwise (if false),
	 * just returns the name of the class, as in "Object".
	 */
	public String getName(boolean withPackage) {
		return (withPackage ? this.pkg + "." : "") + this.name;
	}
	
	/**
	 * Gets the name of the package this class is contained in.
	 */
	public String getPackageName() {
		return this.pkg;
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
				System.out.println(this.getName() + " extends " + this.file.getImport(parent).getName());
		
			//set our parent from its name in import
			this.parent = this.file.getImport(parent);
		
			//with the extension, we need to activate it (ie. process it) before we can use it
			this.parent.activate();
		}
	}

	public void addField(String name, JavaField field) {
		this.fields.put(name, field);
	}

	/**
	 * Adds this class to the list of JavaTypes and adds the primitive
	 * types as well if they don't already exist
	 */	
	private void addType() {
		if (!this.typeList.containsKey("INT")) {
			JavaPrimitive temp = new JavaPrimitive();
			this.typeList.put("INT", temp);
			this.typeList.put("LONG", temp);
			this.typeList.put("FLOAT", temp);
			this.typeList.put("DOUBLE", temp);
			this.typeList.put("CHAR", temp);
			//This probably shouldn't be here
			this.typeList.put("STRING", temp);
		}
		this.typeList.put(this.name,(JavaType)this);
	}
	
	/**
	 * Go through all the parents and get their virtual methods, then just add them.  To do this, we test
	 * if we first have a parent (java.lang.Object doesn't); if we have a parent, grab his virtual methods,
	 * see if we override them (they'll be set in either this.pMethods or this.vMethods), if we don't, then add
	 * them, otherwise, ignore.
	 * 
	 * We can be assured that the parent's virtual methods will be propogated before we call this as, when we activate
	 * the class, the parent's class must finish its activation before we can continue, and as this is part of activation,
	 * we can be sure it will be ready.
	 */
	 private void setupVTable() {
	 	//only do this if we have a parent...otherwise, there will be no methods to add
	 	//java.lang.Object, I'm looking at you.
	 	if (this.parent != null) {
		 	//add the parent's vTable to our own
		 	for (String sig : this.parent.vTable.keySet()) {
		 		if (this.vMethods.containsKey(sig)) //we're overriding a parent method, use ours
		 			this.vTable.put(sig, this.vMethods.get(sig).getParent());
		 		else
		 			this.vTable.put(sig, this.parent.vTable.get(sig));
		 	}
	 	}
	 	
	 	//once we're here, go ahead and add our own methods to the vTable
	 	for (String sig : this.vMethods.keySet()) {
	 		JavaMethod jMethod = this.vMethods.get(sig);
	 		if (!this.vTable.containsKey(sig) && !jMethod.isConstructor())
	 			this.vTable.put(sig, jMethod.getParent());
	 	}
	 	
	 	if (JavaStatic.runtime.test("debug")) {
		 	System.out.println("VTable for " + this.getName());
		 	
		 	//show the methods we have
		 	for (String sig : this.vTable.keySet())
		 		System.out.println("vtbl: " + sig + " --> " + this.vTable.get(sig).getName());
		 	
		 	for (String sig : this.pMethods.keySet())
		 		System.out.println("priv: " + sig);
		 		
		 	System.out.println();
		 }
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
		JavaMethod jMethod = new JavaMethod(n, this);
		
		if (jMethod.isVirtual())
			this.vMethods.put(jMethod.getMethodSignature(), jMethod);
		else
			this.pMethods.put(jMethod.getMethodSignature(), jMethod);
	}
	
	public void visitFieldDeclaration(GNode n) {
		//cannot store the field since a single field declaration
		//might declare multiple fields
		//JavaFieldDec fieldDec = new JavaFieldDec(this, (Node)n);
	}
	
	/**
	 * The default visitor method from Visitor.
	 */
	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}
