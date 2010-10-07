package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.parser.ParseException;

import java.io.File;
import java.io.Reader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Hashtable;
import java.util.ArrayList;

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
class JavaPackages {
	/**
	 * The list of all known classes.
	 * Class name -> Class Object
	 */
	private Hashtable<String, JavaClass> classes = new Hashtable<String, JavaClass>();
	
	/**
	 * The list of all known files.
	 * Filename -> File Object
	 */
	private Hashtable<String, JavaFile> files = new Hashtable<String, JavaFile>();
	
	private JavaPackages() {
		JavaStatic.pkgs = this;
	}
	
	/**
	 * Singleton so that only 1 can exist.
	 */
	private static JavaPackages instance = null;
	 
	public static JavaPackages getInstance() {
		if (instance == null)
			instance = new JavaPackages();
		
		return instance;
	}
	
	/**
	 * =============================================================================================
	 * Methods for adding classes / files to our structure.
	 */
	/**
	 * Nice interface for adding a class and file to our registry at one time.
	 */
	public void addClass(JavaClass cls) {
		this.classes.put(cls.getName(), cls);
	}
	
	public void addFile(JavaFile file) {
		this.files.put(file.getName(), file);
	}
	
	/**
	 * =============================================================================================
	 * Checkers to see if things exist.
	 */
	public boolean classExists(String cls) {
		return this.classes.containsKey(cls);
	}
	
	public boolean fileExists(String file) {
		return this.files.containsKey(file);
	}
	
	/**
	 * =============================================================================================
	 * Nice accessor methods for classes and files.
	 */
	
	/**
	 * Get a file object by a class it contains.
	 */
	public JavaFile getFile(String path) {
		return this.files.get(path);
	}
	
	/**
	 * Get a class object by the name of a class.
	 */
	public JavaClass getClass(String cls) {
		return this.classes.get(cls);
	}
	
	/**
	 * =============================================================================================
	 * Some extra utility stuff.
	 */
	
	public void wrapUp() {
		/*
		for (String cls : this.classes.keySet()) {
			System.out.println("JavaPackges Class: " + cls + " -- " + this.classes.get(cls).getName());
		}
		
		for (String f : this.files.keySet()) {
			System.out.println("JavaPackges File: " + f + " -- " + this.files.get(f).getName());
		}
		*/
	}
	
	/**
	 * Given a package file name, it does its best to import that package file and add it to the translation.
	 *
	 * @param file A string in the format java.lang.Object for the file to import.
	 *
	 * @todo - handle * imports
	 */
	public static void importFile(String file) {
		if (JavaStatic.pkgs.classExists(file))
			return;
		
		//get a file path from our package name
		file = file.replace(".", "/") + ".java";
	
		//see if we can find the file in our search path (given by the "-in" flag)
		try {
			File f = JavaStatic.runtime.locate(file);
			
			//if we get here, then we found our file, so let's throw it to the parser to make sure he gets imported properly
			//so, we're going to restart the process, from the bottom-up, on the file we just found
		
			//run through the imported file and everything it has
			JavaFile javaFile = new JavaFile(f.toString(), Translator.getInstance().parse(JavaStatic.runtime.getReader(f), f));
		} catch (FileNotFoundException e) {
			JavaStatic.runtime.error("File could not be located for import (in JavaFiles.visitImportDeclaration): " + file);
			JavaStatic.runtime.exit(); //abort, we can't possibly go any further
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
}
