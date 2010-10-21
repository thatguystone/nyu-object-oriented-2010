package translator;

import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.Hashtable;
import java.util.HashSet;

import java.util.*;

class JavaFile extends ActivatableVisitor implements Nameable {
	/**
	 * The name of the package this file is in.
	 */
	private String pkg;
	
	/**
	 * The name of the file.
	 */
	private String fileName;
	
	/**
	 * The path to the file.
	 */
	private String path;
	
	/**
	 * Keep a local list of all the classes we have in this one file.
	 */
	private Hashtable<String, JavaClass> classes = new Hashtable<String, JavaClass>();
	
	/**
	 * The list of classes that are imported and ready for use in this file.
	 * Local name of class -> full name of class (Object -> java.lang.Object)
	 */
	private Hashtable<String, String> imports = new Hashtable<String, String>();
	
	/**
	 * Builds a file from a node. Runs dispatch by default so as to cause the list of classes
	 * in the file to be loaded up, imports to be imported, and package to be named.
	 *
	 * @param fileName The name of the file that this object represents.
	 * @param n The structure of everything in the file.
	 */
	JavaFile(String fileName, Node n) {
		this.setFileInfo(fileName);
		
		//we're going to need to know the package, imports, and classes this file has
		//so do that processing on instantiation
		this.dispatch(n);
		
		//and register ourself with JavaPackages
		JavaStatic.pkgs.addFile(this);
		
		//load all of our package / package default imports, we're going to need them
		JavaStatic.pkgs.importMany(this.getPackageName(), fileName);
		
		//setup our default imports that Java provides
		this.defaultImports();
	}
	
	/**
	 * Gets the full name of the file in the form "java.lang.Object".
	 */
	public String getName() {
		return this.pkg + "." + this.fileName;
	}
	
	/**
	 * Gets the name of the package this file is a part of.
	 */
	public String getPackageName() {
		return this.pkg;
	}
	
	/**
	 * Activates all the classes in this file as the file is being marked as used, and we now need them.
	 */
	protected void process() {
		for (JavaClass cls : this.classes.values())
			cls.activate();
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
	 * Sets up our information about the file we are looking at.  By default, it will set the package to "default",
	 * store the path to the file, and extract the file name (from test/test2/some.java, it will extract "some" as
	 * the name).
	 *
	 * @param file The full path to the file (in /location/to/file.java form). 
	 */
	private void setFileInfo(String file) {
		//everything is in the "default" package by default
		//if a package id declared, this will be changed in visitPackageDeclaration()
		this.pkg = "default";
		
		//save the path...not sure why we would need this, but why not?
		this.path = file;
		
		//screw efficiency...iterate that string as often as possible! muahahahaha!
		this.fileName = file.substring((file.lastIndexOf('/') == -1 ? 0 : file.lastIndexOf('/') + 1), file.indexOf(".java"));
	}
	
	/**
	 * Tell the classes that it's their turn to print. (Just intercept one of the print calls.)
	 */
	public void printImplementation() {
		//only print it if the file has been activated
		if (!this.activated)
			return;
		
		for (JavaClass cls : this.classes.values())
			cls.triggerPrint();
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
		for (String cls : f.classes.keySet())
			this.imports.put(cls, f.classes.get(cls).getName());
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
			String cls = (String)itr.next();
			this.imports.put(cls, JavaStatic.pkgs.getClass(pkg + "." + cls).getName());
		}
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
		//see which form of class name we are using
		if (cls.indexOf(".") > -1) {
			return JavaStatic.pkgs.getClass(cls);
		} else {
			//first, check our local classes
			if (this.classes.containsKey(cls))
				return this.classes.get(cls);
			
			if (this.imports.containsKey(cls))
				return JavaStatic.pkgs.getClass(this.imports.get(cls));
				
			//well, we didn't find it anywhere else, so give the current package a shot
			if (JavaStatic.pkgs.classInPackage(this.pkg, cls))
				return JavaStatic.pkgs.getClass(this.pkg + "." + cls);
		}
		
		/*/
		for (StackTraceElement e : Thread.currentThread().getStackTrace())
			System.out.println(e.toString());
		
		System.out.println();
		System.out.println();
		//*/
		
		//wtf? error...
		JavaStatic.runtime.error("Class could not be found for import (in JavaFile.getImport): " + cls);
		JavaStatic.runtime.exit(); //abort, we can't possibly go any further
		
		return null; //oh, shutup already, Java...yes, you need a return statement. w/e.
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

		this.pkg = dec.substring(1);
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
		JavaClass cls = new JavaClass(this, this.pkg, (Node)n);
		
		//save a copy of the class locally
		this.classes.put(cls.getName(false), cls);
	}
}
