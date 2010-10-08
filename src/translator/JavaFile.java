package translator;

import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.Hashtable;

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
	 */
	private Hashtable<String, String> imports = new Hashtable<String, String>();

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
	
	public String getName() {
		return this.pkg + "." + this.fileName;
	}
	
	public String getPackageName() {
		return this.pkg;
	}
	
	/**
	 * Activates all the classes in this file as the file is being marked as used, and we now need them
	 */
	protected void process() {
		for (JavaClass cls : this.classes.values())
			cls.activate();
	}
	
	/**
	 * Setup our default imports.
	 */
	private void defaultImports() {
		this.importFile("java.lang.Object");
		//this.importFile("java.lang.System");
	}
	
	/**
	 * Sets the current package we are dealing with.  If the package name ends in ".java", it assumes
	 * that the current package is part of the "default" package, and the package is set to "default".
	 * Otherwise, the package is st to whatever is passed in.
	 * 
	 * @param pkg The name of the package / file to be set.
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
	 * ==================================================================================================
	 * Utility methods
	 */
	
	/**
	 * Imports a module into this file given something like "java.lang.Object".
	 */
	private void importFile(String pkg) {
		JavaFile f = JavaStatic.pkgs.importFile(pkg);
		
		//grab the entries in the imported file's class entries and add them to our imports
		for (String cls : f.classes.keySet())
			this.imports.put(cls, f.classes.get(cls).getName()); 
	}
	
	public JavaClass getImport(String cls) {
		if (this.imports.containsKey(cls))
			return JavaStatic.pkgs.getClass(this.imports.get(cls));
		
		return null;
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
	
	public void visitImportDeclaration(GNode n) {
		String pkg = "";
		
		GNode imp = (GNode)n.get(1);
		for (int i = 0; i < imp.size(); i++)
			pkg += "." + imp.get(i); //append to our full package name
		
		//lose the first dot
		pkg = pkg.substring(1);
		
		this.importFile(pkg);
		
		/**
		 * @todo - handle * imports
		 */
		 
		/**
		 * Get the names of all the classes contained inside this import,
		 * then pass that on to all of the classes for their resolution
		 */
	}
	
	public void visitClassDeclaration(GNode n) {
		//we know that we will have our package set properly before we start building any classes
		//as java forces package declarations to come before any class declarations, and we are 
		//assuming that we are only getting well-formed java files
		JavaClass cls = new JavaClass(this, this.pkg, (Node)n);
		
		//save a copy of the class locally
		this.classes.put(cls.getName(false), cls);
	}
	
	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}
