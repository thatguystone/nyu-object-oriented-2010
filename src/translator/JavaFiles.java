package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.parser.ParseException;

import java.io.File;
import java.io.Reader;
import java.io.IOException;

import java.util.Hashtable;

/**
 * Provides a nice interface to Translator.  Rather than having everything in Translator, we come here
 * and do all of the processing.  This serves as a bridge from what the user wants to what needs to happen:
 * here is where the hard work begins.  We are given files to translate by Translator, and this takes care of
 * it.
 *
 * On a deeper level, this is really only a wrapper to a Hashtable with a list of all the defined classes.
 * We keep those here in order to have quick access to them from everywhere.  Don't let the name fool you:
 * this is for processing java files from translator, but it's little more than a nice interface.
 */
class JavaFiles {
	/**
	 * The list of all known classes.
	 */
	private Hashtable<String, JavaClass> classes = new Hashtable<String, JavaClass>();
	
	private JavaFiles() {
		JavaStatic.javaFiles = this;
	}
	
	/**
	 * Singleton so that only 1 can exist.
	 */
	private static JavaFiles instance = null;
	 
	public static JavaFiles getInstance() {
		if (instance == null)
			instance = new JavaFiles();
		
		return instance;
	}
	
	public void process(String file, Node n) {
		JavaFile f = new JavaFile();
		f.process(file, n);
	}
	
	/**
	 * Blindly add a class into our list.  We assume that it compiles, and since java
	 * doesn't allow duplicate class names, we're all in the clear for just throwing it
	 * in there.
	 */
	public void addClass(String name, JavaClass cls) {
		this.classes.put(name, cls);
	}
	
	public boolean classExists(String cls) {
		return this.classes.containsKey(cls);
	}
	
	public void wrapUp() {
		for (String key : this.classes.keySet()) {
			System.out.println("Found and processed: " + this.classes.get(key).getName());
		}
	}
}

class JavaFile extends Visitor {
	/**
	 * The search path for our files.
	 */
	private static String[] searchPath = new String[]{
		System.getProperty("user.dir"), //the directory with the test files
		System.getProperty("user.dir") + "/../src/" //the directory with the re-implemented java source files
	};
	
	/**
	 * The name of the package we are currently working on.
	 */
	private String currentPackage;
	
	/**
	 * The name of the file.
	 */
	private String fileName;

	public void process(String file, Node n) {
		//processing a new file, reset the package
		this.setFileInfo(file);
		
		//and go!
		this.dispatch(n);
	}
	
	/**
	 * Sets the current package we are dealing with.  If the package name ends in ".java", it assumes
	 * that the current package is part of the "default" package, and the package is set to "default".
	 * Otherwise, the package is st to whatever is passed in.
	 * 
	 * @param pkg The name of the package / file to be set.
	 */
	private void setFileInfo(String pkg) {
		if (pkg.indexOf(".java") == (pkg.length() - 5)) {
			this.currentPackage = "default";
			
			//screw efficiency...iterate that string as often as possible! muahahahaha!
			this.fileName = pkg.substring((pkg.lastIndexOf('/') == -1 ? 0 : pkg.lastIndexOf('/') + 1), pkg.indexOf(".java"));
		} else {
			this.currentPackage = pkg;
		}
	}
	
	/**
	 * For the package declaration -- sets the current package we are looking at.
	 */
	public void visitPackageDeclaration(GNode n) {
		String dec = "";
		
		//build the name of the package
		GNode pkg = (GNode)n.get(1);
		for (int i = 0; i < pkg.size(); i++)
			dec += "." + pkg.get(i);
		
		this.setFileInfo(dec.substring(1));
	}
	
	public void visitClassDeclaration(GNode n) {
		//we know that we will have our package set properly before we start building any classes
		//as java forces package declarations to come before any class declarations, and we are 
		//assuming that we are only getting well-formed java files
		JavaClass cls = new JavaClass(this.currentPackage, n);
		
		//add to our list of classes
		//(add here, before it's processed, so that circular dependencies don't become infinite recursion)
		JavaStatic.javaFiles.addClass(cls.getName(), cls);
		
		//visit the class
		cls.process((Node)n);
	}
	
	public void visitImportDeclaration(GNode n) {
		String pkg = "";
		
		GNode imp = (GNode)n.get(1);
		for (int i = 0; i < imp.size(); i++) {
			pkg += "." + imp.get(i); //append to our full package name
		}
		
		JavaFile.importPkg(fileName);
	}
	
	/**
	 * Given a package file name, it does its best to import that package file and add it to the translation.
	 *
	 * @todo - handle * imports
	 */
	public static void importPkg(String pkg) {
		//get a file path from our package name
		String file = pkg.replace(".", "/") + ".java";
	
		//see if we can find the file in our search path
		File f = null;
		for (String p : JavaFile.searchPath) {
			f = new File(p + file);
			if (f.exists())
				break; //stop this pointless exercise in looping
		}
		
		//this should never happen (unless someone forgets to implement something)
		//...but let's just be sure!
		if (f == null || !f.exists()) {
			JavaStatic.runtime.error("File could not be located for import (in JavaFiles.visitImportDeclaration): " + f.toString());
			JavaStatic.runtime.exit(); //abort, we can't possibly go any further
		}
		
		//if we get here, then we found our file, so let's throw it to the parser to make sure he gets imported properly
		//so, we're going to restart the process, from the bottom-up, on the file we just found
		
		//run through the imported file and everything it has
		JavaFile javaFile = new JavaFile();
		
		//shameless copy/paste job from xtc.util.Tool.run() for running parse on files.
		try {
			javaFile.process(f.toString(), Translator.getInstance().parse(JavaStatic.runtime.getReader(f), f));
		} catch (IOException x) {
			if (null == x.getMessage())
				JavaStatic.runtime.error("I/O error");
			else
				JavaStatic.runtime.error(x.getMessage());
		} catch (ParseException x) {
			JavaStatic.runtime.error();
			System.err.print(x.getMessage());
		}
	}
	
	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}
