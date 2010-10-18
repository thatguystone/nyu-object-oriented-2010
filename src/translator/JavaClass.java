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
	 * The parent class.  This is going to be something from "extends" or java.lang.Object.
	 */
	private JavaClass parent;

	/**
	 * keeps track of how many packages(namespaces) deep we are to know how many .close()'s to use.
	 */
	private int scopeDepth = 1;

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
	 * List of all fields in the class.
	 * Field name -> Field Object
	 * Moved to JavaScope.
	 */
	//private LinkedHashMap<String, JavaField> fields = new LinkedHashMap<String, JavaField>();
	
	/**
	 * Builds a class from a node.
	 *
	 * @param file The file that contains this class (the "parent file").
	 * @param pkg The name of the package this class is contained in.
	 * @param n The node that contains the defintion for this class.
	 */
	JavaClass(JavaFile file, String pkg, Node n) {
		this.pkg = pkg;
		this.name = (String)n.get(1);
		this.setFile(file);
		this.setScope(file);
		this.setup(n);
		
		//and register ourself with JavaPackages
		JavaStatic.pkgs.addClass(this);
	}			

	/**
	 * Only to be used upon activation.  Does everything we need to get this class ready for translation.
	 */
	protected void process() {
		//go for a nice visit to see everyone
		this.dispatch(this.node);
		
		//activate the parent file so that all the classes with him are included
		this.file.activate();
		
		if (JavaStatic.runtime.test("debug"))
			System.out.println("Class activated: " + this.getName());
		
		//check if we have a parent; if we don't, then java.lang.Object is our parent
		this.setParent("java.lang.Object");
		
		//once we're sure we have a parent, then add all our inherited methods
		this.setupVTable();

		//find out how many packages deep we are
		this.setDepth();

		//add ourself to the print queue AFTER all dependencies have been activated/added
		this.registerPrint(this.pkg);
	}
	
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
	 * Classes need to implement this themselves because the scope hierarchy terminates here.
	 */	
	public String getScopeString() {
		return this.getName();
	}

	/**
	 * Gets the name of the package this class is contained in.
	 */
	public String getPackageName() {
		return this.pkg;
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
	 * Set our package depth.
	 * You may ask yourself "why is this here".
	 * Only classes care about namespace declarations.
	 */
	private void setDepth() {
		String temp = this.pkg;
		while (temp.indexOf('.') != -1) {
			String firstHalf = temp.substring(0, temp.indexOf('.'));
			String secondHalf = temp.substring(temp.indexOf('.') + 1, temp.length());
			temp = firstHalf + "::" + secondHalf;
			this.scopeDepth++;
		}
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
	 * Print out the VTable to the header.
	 * This is just a jumbled mess...isn't there a neater way to do this?
	 *
	 * For one, you can let JavaMethod format itself to be printed and all you'd
	 * need here is something like block.pln(jMeth.printMe());.
	 */
	protected void printHeader() {

		//print the class prototypes and typedef
		CodeBlock proto = getProto();
		proto = proto
			.pln("struct __" + this.getName(false))
			.pln("struct __" + this.getName(false) + "_VT")
			.pln()
			.pln("typedef __" + this.getName(false) + "* " + this.getName(false) + ";");
		for (int j = 0; j < scopeDepth; j++)
			proto = proto.close();
		
		CodeBlock block = this.hBlock("namespace " + this.pkg);
		
		block = block
			.block("struct __" + this.getName(false))
					.pln("__" + this.getName(false) + "_VT* __vptr;")
					.pln()
					.pln("__" + this.getName(false) + "() :")
						.block("__vptr(&__vtable)").close()
					.pln("static Class __class();")
		;

					for (JavaField fld : this.fields.values())
						block.pln(fld.printDec());
					block.pln();
		
					//now, dump out all of our virtual
					for (JavaMethod jMeth : this.vMethods.values())
						block.pln("static " + jMeth.getMethodHeader() /*jMeth.getCReturnType() + " " + jMeth.getCMethodSignature(this.getName(false)) + ";"*/);
		
					//and now for those static and private methods
					for (JavaMethod jMeth : this.pMethods.values())
						if (jMeth.getName().compareTo("main") != 0)
							block.pln("static " + jMeth.getMethodHeader() /*jMeth.getCReturnType() + " " + jMeth.getCMethodSignature(this.getName(false)) + ";"*/);
		
		block =
			block.close()
			.block("struct __" + this.getName(false) + "_VT")
				.pln("Class __isa;")
		;
				
				//print out the methods in the vtable
				for (String meth : this.vTable.keySet()) {
					JavaMethod jMeth = this.vTable.get(meth).getMethod(meth);
					block.pln("static " + jMeth.getCReturnType() + " " + jMeth.getCMethodType(/*this.getName(false))*/this) + ";");
				}
				
				//and now print the vtable constructor
				block = block
					.pln()
					.block("__" + this.getName(false) + "_VT() :")
					.pln("__isa(__" + this.getName(false) + "::__class()),")
				;
				
				//and then the initializors
				int i = 1, len = this.vTable.size();
				for (String meth : this.vTable.keySet()) {
					JavaClass cls = this.vTable.get(meth);
					JavaMethod jMeth = this.vTable.get(meth).getMethod(meth);
			
					//do we need to cast our function pointer?
					if (cls.equals(this)) {
						block.pln(jMeth.getName() + "(&__" + this.getName(false) + "::" + jMeth.getName() + ")" + (i == len ? " {" : ","));
					} else { //nope, we're looking at inheritance, so cast
						block.pln(
							jMeth.getName() + "(" + jMeth.getCMethodCast(/*this.getName(false)*/this) +
							this.getCppScopeTypeless(this,cls) + "&__" + cls.getName(false) + "::" + jMeth.getName() + ")" + (i == len ? " {" : ",")
						);
					}
				
					i++;
				}
		
		block =
				block.close()
			.close();
		//close our namespaces
		for (int j = 0; j < scopeDepth; j++)
			block = block.close();
	}

	/**
	 * Print the cpp file implementation.
	 * Really just opens a namespace and gets its methods to print their implementation.
	 */
	protected void printImplementation() {
		CodeBlock block = this.cppBlock;

		//let each method attach it's own block onto our block
		for (JavaMethod jMeth : this.vMethods.values())
			jMeth.getMethodBlock(block);
		//now for the rest, except main
		for (JavaMethod jMeth : this.pMethods.values())
			if (jMeth.getName().compareTo("main") != 0)
				jMeth.getMethodBlock(block);

		//close our namespaces
		for (int j = 0; j < scopeDepth; j++)
			block = block.close();

	}

	/**
	 * Add a field to our field list
	 */
	/*public void addField(JavaField field) {
		this.fields.put(field.getName(), field);
	}*/

	/**
	 * Check if this class has this field.
	 */
	public boolean hasField(String field) {
		if (this.fields.containsKey(field))
			return true;
		return false;
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
		JavaMethod jMethod = new JavaMethod(n, this.getFile(), this);
		
		if (jMethod.isNative())
			JavaStatic.pkgs.importNative(this.getName());
		else if (jMethod.isVirtual())
			this.vMethods.put(jMethod.getMethodSignature(), jMethod);
		else
			this.pMethods.put(jMethod.getMethodSignature(), jMethod);
	}
}
