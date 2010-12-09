package translator;

import java.util.Hashtable;
import java.util.Iterator;

import translator.Printer.CodeBlock;
import translator.Printer.PrintOrder;
import xtc.tree.Node;
import xtc.tree.GNode;

/**
 * A representation of a java file.
 */
public class JavaFile extends ActivatableVisitor implements Nameable {
	/**
	 * The name of the file.
	 */
	private String fileName;
	
	/**
	 * Keep a local list of all the classes we have in this one file.
	 */
	private Hashtable<String, JavaClass> classes = new Hashtable<String, JavaClass>();
	
	/**
	 * The list of classes that are imported and ready for use in this file. This only handles external
	 * classes that have been imported; none of the package classes are here.
	 * Local name of class -> JavaClass (Object -> JavaClass)
	 */
	private Hashtable<String, JavaClass> imports = new Hashtable<String, JavaClass>();
	
	/**
	 * Store the file name for the file and pass off the node for future use.
	 */
	JavaFile(String fileName, GNode n) {
		//pass the node up, otherwise we get some implicit constructor being called, which, of
		//course, doesn't exist, so we get a compile time error. w/e.  shut up java!
		super(n);
		
		//ok, now that java's stupidity is done with, moving on...
		this.setFileInfo(fileName);

		//we're going to need to know the package, imports, and classes this file has
		//so do that processing on instantiation
		//this is safe to do as files CAN'T have fields, declarations, etc
		this.dispatch(n);
		
		//and register ourself with JavaPackages
		JavaStatic.pkgs.addFile(this);
		
		//load all of our package / package default imports, we're going to need them
		JavaStatic.pkgs.importMany(this.getPackageName(), this.fileName);
		
		//setup our default imports that Java provides
		this.defaultImports();
	}
	
	/**
	 * Sets up our information about the file we are looking at.  By default, it will set the package to "default",
	 * store the path to the file, and extract the file name (from test/test2/some.java, it will extract "some" as
	 * the name).
	 *
	 * @param file The full path to the file (in /location/to/file.java form). 
	 */
	private void setFileInfo(String file) {
		//everything is in the "default" package by default
		//if a package id declared, this will be changed in visitPackageDeclaration()
		this.setPackageName("defaultPkg");
		
		//screw efficiency...iterate that string as often as possible! muahahahaha!
		this.fileName = file.substring((file.lastIndexOf('/') == -1 ? 0 : file.lastIndexOf('/') + 1), file.indexOf(".java"));
	}
	
	/**
	 * Setup our default imports.  Anything that is imported by default into a file by java should be
	 * listed here so that we can correctly simulate a java environment.
	 */
	private void defaultImports() {
		this.importFile("java.lang.Object");
		this.importFile("java.lang.String");
		this.importFile("java.lang.System");
		this.importFile("java.lang.Class");
	}
	
	/**
	 * Activates all the classes in this file as the file is being marked as used, and we now need them.
	 */
	protected void process() {
		for (JavaClass cls : this.classes.values())
			cls.activate();
	}
	
	/**
	 * Prints out the junk for 1 class. We do not print the entire file because there are order activation issues:
	 * JavaClass handles getting everything to activate and process in the right order, so we only take a command
	 * to print out a single class in our file.  
	 */
	public void print(JavaClass cls) {
		//the simplest way to get the namespaces injected into all these blocks...fun
		CodeBlock proto = new CodeBlock(), header = new CodeBlock(), implm = new CodeBlock();
		CodeBlock origProto = proto, origHeader = header, origImplm = implm;
		
		//setup our namespaces
		String[] namespaces = this.getPackageName().split("\\.");
		for (String n : namespaces) {
			proto = proto.block("namespace " + n);
			header = header.block("namespace " + n);
			implm = implm.block("namespace " + n);
		}
		
		//print out our given class
		cls.print(proto, header, implm);
		
		//make sure the namespace declarations are closed
		implm.closeAll();
		proto.closeAll();
		header.closeAll();
		
		//and queue our blocks for printing
		//the cpp block wasn't printing, so I just stuck it in the header block for the time being
		JavaStatic.cpp.p(PrintOrder.IMPLEMENTATION, origImplm);
		JavaStatic.h.p(PrintOrder.PROTOTYPE, origProto);
		JavaStatic.h.p(PrintOrder.HEADER, origHeader);
	}
	
	/**
	 * Set the default visibility for this scope item. Defaults to PackageProtected if not overriden.
	 */
	protected Visibility getDefaultVisibility() {
		return Visibility.PUBLIC;
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
		
		return name + this.fileName; 
	}
	
	public String getCppName() {
		return this.getCppName(true);
	}
	
	public String getCppName(boolean fullName) {
		return "JavaFile does not have a name in C++.  This isn't going to compile anymore.";
	}
	
	/**
	 * Searches for a class that has been imported into this file.  Given the name of a class, in "Object"
	 * form, it will search its local imports to expand to the full name of the class (ie. expand "Object"
	 * to "java.lang.Object") and return the {@link JavaClass} associated with that import.
	 *
	 * If given the name of the class in "java.lang.Object" form, it will merely run to {@link JavaPackages#getClass}
	 * to get the class.
	 *
	 * @param cls The name of the class to get, in the "Object" or "java.lang.Object" form.  In other words, any way
	 * that it could be typed in the code.
	 */
	public JavaClass getImport(String cls) {
		JavaClass retCls = null;
	
		//see which form of class name we are using
		if (cls.indexOf(".") > -1) {
			retCls = JavaStatic.pkgs.getClass(cls);
		} else {
			//first, check our local classes
			if (this.classes.containsKey(cls))
				retCls = this.classes.get(cls);
			
			else if (this.imports.containsKey(cls))
				retCls = this.imports.get(cls);
				
			//well, we didn't find it anywhere else, so give the current package a shot
			else if (JavaStatic.pkgs.classInPackage(this.getPackageName(), cls))
				retCls = JavaStatic.pkgs.getClass(this.getPackageName() + "." + cls);
		}
		
		//if we have a class, activate him as he is going to be used.
		if (retCls != null)
			retCls.activate();
		
		return retCls;
	}
	
	/**
	 * ==================================================================================================
	 * Utility methods
	 */
	
	/**
	 * Imports a module into this file given something like "java.lang.Object".  This is the main handler
	 * for all import statements.
	 *
	 * @param pkg The name of a file to import, in "java.lang.Object" form.
	 */
	private void importFile(String pkg) {
		JavaFile f = JavaStatic.pkgs.importFile(pkg);
		
		//grab the entries in the imported file's class entries and add them to our imports
		for (String cls : f.classes.keySet()) {
			JavaClass jCls = f.classes.get(cls);
			if (jCls.isVisible(Visibility.PUBLIC)) {
				this.imports.put(cls, jCls);
			}
		}
	}
	
	/**
	 * Used for importing * declarations.
	 *
	 * @param pkg The name of the package from which to import all files.
	 */
	private void importPackage(String pkg) {
		JavaStatic.pkgs.importMany(pkg);
		
		Iterator itr = JavaStatic.pkgs.getPackage(pkg).iterator();
		while (itr.hasNext()) {
			JavaClass cls = JavaStatic.pkgs.getClass(pkg + "." + (String)itr.next());
			if (cls.isVisible(Visibility.PUBLIC))
				this.imports.put(cls.getName(false), cls);
		}
	}
	
	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
	/**
	 * For the package declaration -- sets the current package we are looking at.
	 */
	public void visitPackageDeclaration(GNode n) {
		//build the name of the package
		GNode pkg = (GNode)n.get(1);
		String dec = "";
		for (int i = 0; i < pkg.size(); i++)
			dec += "." + pkg.get(i);
		
		this.setPackageName(dec.substring(1));
	}
	
	/**
	 * Grabs the imports and imports them into the file using {@link #importFile(String)}
	 */
	public void visitImportDeclaration(GNode n) {
		String pkg = "";
		
		GNode imp = (GNode)n.get(1);
		for (int i = 0; i < imp.size(); i++)
			pkg += "." + imp.get(i); //append to our full package name
		
		//lose the first dot
		pkg = pkg.substring(1);
		
		if (n.get(2).toString().equals("*"))
			this.importPackage(pkg);
		else
			this.importFile(pkg);
	}
	
	/**
	 * Adds a class to the file.
	 */
	public void visitClassDeclaration(GNode n) {
		//we know that we will have our package set properly before we start building any classes
		//as java forces package declarations to come before any class declarations, and we are 
		//assuming that we are only getting well-formed java files
		JavaClass cls = new JavaClass((JavaScope)this, n);
		
		//save a copy of the class locally
		this.classes.put(cls.getName(false), cls);
	}
}
