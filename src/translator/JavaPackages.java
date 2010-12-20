package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.parser.ParseException;
import xtc.util.Runtime;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Reader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Hashtable;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import translator.Printer.CodeBlock;
import translator.Printer.CodePrinter;

/**
 * Provides a nice interface to Translator.  Rather than having everything in Translator, we come here
 * and do all of the processing.  This serves as a bridge from what the user wants to what needs to happen:
 * here is where the hard work begins.  We are given files to translate by Translator, and this takes care of
 * it.
 * 
 * Rather than creating a tree-like structure for our class heirachy, I'm going to
 * store everything in Hashtables so that we can do quick lookups based on names
 * alone, rather than having to traverse and find.  Everything in java (as with
 * any language), is represented by a string, so this is the quickest and easiest
 * way to store all our information.
 */
public class JavaPackages {
	/**
	 * The list of all imported packages.
	 * Name of package -> list of classes in that package
	 */
	private Hashtable<String, HashSet<String>> packages = new Hashtable<String, HashSet<String>>(); 
	
	/**
	 * The list of all known files.
	 * Filename -> File Object
	 */
	private Hashtable<String, JavaFile> files = new Hashtable<String, JavaFile>();
	
	/**
	 * The list of all known classes.
	 * Class name -> Class Object
	 */
	private Hashtable<String, JavaClass> classes = new Hashtable<String, JavaClass>();
	
	/**
	 * All of the packages that we have loaded in their entirety.
	 * We can't rely on packages for this information because it is populated when even 1 class
	 * is loaded, not when all are.
	 */
	public HashSet<String> loadedPackages = new HashSet<String>();
	
	/**
	 * The list of classes that contain native methods that we have already loaded.
	 */
	public HashSet<String> loadedNatives = new HashSet<String>();
	
	/**
	 * Singleton so that only 1 can exist.
	 */
	private static JavaPackages instance = null;
	
	/**
	 * The main method that should be called.  There can only ever be 1 per translation, so all we need is this.
	 */
	private JavaMethod mainMethod = null;
	
	/**
	 * Don't allow creation of the class anywhere but inside itself.
	 */
	private JavaPackages() {
		JavaStatic.pkgs = this;
	}
	
	/**
	 * Get the only instance of this class.
	 */
	public static JavaPackages getInstance() {
		if (instance == null)
			instance = new JavaPackages();
		
		return instance;
	}
	
	public void wrapUp() {
		this.getClass("java.lang.ClassCastException").activate();
		this.getClass("java.lang.NullPointerException").activate();
		
		//print out our main method to the C++ file
		//die if we don't have a main...that's clearly an error
		if (this.mainMethod == null) {
			JavaStatic.runtime.error("JavaPackages: no main method found.");
			JavaStatic.runtime.exit();
		}
		
		CodeBlock main = new CodeBlock();
		
		//i'm gonna give that bitch a main function
		//bitches love main functions
		main
			.block("int main(int argc, char** argv)")
				.pln("ARRAY(java::lang::String) args = new java::util::__Array<java::lang::String>(1, argc);")
				.pln()
				.block("for (int i = 0; i < argc; i++)", false)
					.pln("args->get(1, i) = new java::lang::__String(argv[i]);")
				.close()
				.pln()
				.pln(this.mainMethod.getCppName(true, false) + "(args);")
			.close()
		;
		
		JavaStatic.cpp.p(CodePrinter.PrintOrder.IMPLEMENTATION, main);
	}
	
	/**
	 * Sets the main method that we find in our translation.  This can only be set once, and it will issue
	 * a warning if called more than once.
	 */
	public void setMainMethod(JavaMethod m) {
		if (this.mainMethod == null)
			this.mainMethod = m;
		else
			JavaStatic.runtime.warning("JavaPackages: Attempt to set main method more than once; ignoring...");
	}
	
	/**
	 * =============================================================================================
	 * Methods for adding classes / files to our structure.
	 */
	 
	/**
	 * Adds a class (and its package) to the registry.
	 *
	 * @param cls The class to add to our registry.
	 */
	public void addClass(JavaClass cls) {
		//stash the class
		this.classes.put(cls.getName(), cls);
		
		//add the class to our package
		if (!this.packageExists(cls.getPackageName()))
			this.packages.put(cls.getPackageName(), new HashSet<String>());
		
		//add the class to its package
		this.packages.get(cls.getPackageName()).add(cls.getName(false)); 
	}
	
	/**
	 * Adds a file to the registry.
	 *
	 * @param file The file to be added.
	 */
	public void addFile(JavaFile file) {
		this.files.put(file.getName(), file);
	}
	
	/**
	 * =============================================================================================
	 * Checkers to see if things exist.
	 */
	
	/**
	 * Tests if the class exists.
	 *
	 * @param cls The name of a class, in the "java.lang.Object" form.
	 */
	public boolean classExists(String cls) {
		return this.classes.containsKey(cls);
	}
	
	/**
	 * Tests if the given file is registered (ie. exists).
	 *
	 * @param file The name of a file, in the "java.lang.Object" form.
	 */
	public boolean fileExists(String file) {
		return this.files.containsKey(file);
	}
	
	/**
	 * Tests if the package exists.
	 *
	 * @param pkg The name of the package, in the "java.lang" form.
	 */
	public boolean packageExists(String pkg) {
		return this.packages.containsKey(pkg);
	}
	
	/**
	 * =============================================================================================
	 * Nice accessor methods for classes and files.
	 */
	
	/**
	 * Get a file object by a class it contains.
	 *
	 * @param path The name of a file, in the "java.lang.Object" form.
	 */
	public JavaFile getFile(String path) {
		return this.files.get(path);
	}
	
	/**
	 * Get a class object by the name of a class.
	 *
	 * @param cls The name of a class, in the "java.lang.Object" form.
	 */
	public JavaClass getClass(String cls) {
		return this.classes.get(cls);
	}
	
	/**
	 * Get a list of all the defined classes (as strings) contained in the given package.
	 *
	 * @param pkg The name of the package, in the "java.lang" form.
	 */
	public HashSet<String> getPackage(String pkg) {
		return this.packages.get(pkg);
	}
	
	/**
	 * Test to see if a given package has a class.
	 *
	 * @param pkg The name of the package.
	 * @param cls The name of the class.
	 */
	public boolean classInPackage(String pkg, String cls) {
		if (this.packages.containsKey(pkg))
			return this.packages.get(pkg).contains(cls);
		
		return false;
	}
	
	/**
	 * =============================================================================================
	 * Some extra utility stuff.
	 */
	 
	/**
	 * When we have a method marked as native, we need to import its corresponding .cpp and .h implementation files.
	 * We allow methods to be marked as native in accordance with the Apache Harmony with implementation of native:
	 * they use the @api2vm tag to flag a class as doing calls directly to the vm and not having implementations. This
	 * differs from how java typically handles native (requires you to use some class loading stuff), but I believe it
	 * to be a reasonable alternative for our purposes. Also, we do NOT support using native for any translations;
	 * this is just for our internal libraries / java api.
	 */
	public void importNative(JavaClass cls, CodeBlock header, CodeBlock implm) {
		String clsName = cls.getName();
		if (this.loadedNatives.contains(clsName))
			return;
		
		this.loadedNatives.add(clsName);
		
		String cppFile = clsName.replace(".", "/") + ".cpp";
		String hFile = clsName.replace(".", "/") + ".h";
		String overloadFile = clsName.replace(".", "/") + ".overload.cpp";
		
		//first, we're going to attempt to find the .cpp and .h files that correspond with this class
		this.appendFileToOutput(cppFile, JavaStatic.cpp, implm, cls);
		this.appendFileToOutput(hFile, JavaStatic.h, header, cls);
		this.appendFileToOutput(overloadFile, JavaStatic.cpp, implm, cls, true);
	}
	
	/**
	 * Given the pointer to a file, it outputs the contents of the file to the proper output file.
	 *
	 * @param f The file to read from.
	 * @param printer The output to print to.
	 * @param block The block into which to print.
	 * @param cls The class that requested printing.
	 */
	public void appendFileToOutput(String f, CodePrinter printer, CodeBlock block, JavaClass cls) {
		this.appendFileToOutput(f, printer, block, cls, false);
	}
	 
	/**
	 * Given the pointer to a file, it outputs the contents of the file to the proper output file.
	 *
	 * @param f The file to read from.
	 * @param printer The output to print to.
	 * @param block The block into which to print.
	 * @param cls The class that requested printing.
	 * @param before If all the contents of the file should be printed out of namespace blocks to the beginning of the file
	 */
	public void appendFileToOutput(String f, CodePrinter printer, CodeBlock block, JavaClass cls, boolean before) {
		try {
			Scanner file = new Scanner(JavaStatic.runtime.locate(f));
			
			int iOverload;
			int iOpenBracket, iCloseBracket;
			while (file.hasNextLine()) {
				String line = file.nextLine();
				
				if (before || line.startsWith("#")) {
					printer.b_pln(line);
				} else {
					//attempt to do overload replacement of the method names in the native files
					while ((iOverload = line.indexOf("{overload:")) > -1) {
						iOpenBracket = line.indexOf("[");
						iCloseBracket = line.indexOf("]");
						
						//10 = length of "{overload:"
						String name = line.substring(iOverload + 10, iOpenBracket);
						JavaMethodSignature sig = new JavaMethodSignature();
						
						//loop through our arguments and add them to our signature
						String args = line.substring(iOpenBracket + 1, iCloseBracket);
						if (args.length() > 0) {
							for (String t : args.split(","))
								sig.add(JavaType.getType(cls, t.trim()), cls);
						}
						
						JavaMethod m = cls.getClassMethod(name, sig);
						
						//if we didn't find the method, then that is an error in the native.cpp file, so send a warning
						if (m == null)
							JavaStatic.runtime.warning("In native file \"" + f + "\", the overloaded method \"" + name + "\" was not defined in the class definition");
						else
							line = line.replace(line.substring(iOverload, line.indexOf("}") + 1), m.getCppName(false));
					}
					
					block.pln(line);
				}
			}
		} catch (FileNotFoundException e) { } //doesn't matter if we couldn't find the file, maybe they just didn't want to have it
	}

	/**
	 * Given a package file name, it does its best to import that package file and add it to the translation.
	 *
	 * @param file A string in the format java.lang.Object for the file to import.
	 */
	public JavaFile importFile(String file) {
		if (this.fileExists(file))
			return this.getFile(file);
		
		//get a file path from our package name
		String jFile = file.replace("default.", "").replace(".", "/") + ".java";
		
		//see if we can find the file in our search path (given by the "-in" flag)
		try {
			File f = JavaStatic.runtime.locate(jFile);
			
			//if we get here, then we found our file, so let's throw it to the parser to make sure he gets imported properly
			//so, we're going to restart the process, from the bottom-up, on the file we just found
			
			//run through the imported file and everything it has
			JavaFile javaFile = new JavaFile(f.toString(), (GNode)JavaStatic.translator.parse(JavaStatic.runtime.getReader(f), f));
			
			return javaFile;
		} catch (FileNotFoundException e) {
			JavaStatic.runtime.error("File could not be located for import (in JavaPackages.importFile): " + file);
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
		
		return null; //silence, Java!
	}
	
	/**
	 * Loads a package when we're sure that it will be in the search path.
	 *
	 * @see translator#importMany(String, String)
	 * 
	 * @param pkg The name of the package to import, in "java.lang.Object" form.
	 */
	public void importMany(String pkg) {
		this.importMany(pkg, null);
	}
	
	/**
	 * Loads a full package.  When we're loading the default package, we need the filename that caused the package
	 * to load as "default" is not actually a path that we can just load.  If it's not the default package, then it 
	 * should be able to find itself in the package search path.
	 * 
	 * Done with file as an extra parameter so as not to compromise look-ups for all imports (if we just passed in the 
	 * file and hoped that it could resolve, it would fail: how do we resolve java.lang from some random test.pkg?).
	 *
	 * @param pkg The name of the package to import, in "java.lang.Object" form.
	 * @param file The path to the file that caused the import (only important for "default" packages).
	 */
	public void importMany(String pkg, String file) {
		//this is kinda janky, but "packages" can contain partially-loaded packages, so we can't rely on it
		//thus, we needed another data structure to confirm when something has been through here (ie. completely loaded)
		if (this.loadedPackages.contains(pkg))
			return;
		
		//make sure we don't load the package again
		this.loadedPackages.add(pkg);
		
		//the directory that contains the package
		File dir = null;
		
		//if we're loading the default package
		if (pkg == "default") {
			file = new File(file).getAbsolutePath();
			//attempt to remove the file name from the path
			dir = new File(file.substring(0, file.lastIndexOf("/")));
			
			//if we failed at finding the package root
			if (!dir.isDirectory())
				dir = null;
		} else {
			//we need to look for a directory, and xtc.runtime.locate() doesn't do that
			List<File> dirs = JavaStatic.runtime.getFileList(Runtime.INPUT_DIRECTORY); 
		
			//if no source dirs were specified on the command line
			if (dirs == null)
				return;
			
			//prepare to go to the filesystem
			String jPkg = pkg.replace(".", "/");
		
			for (File root : dirs) {
				dir = new File(root, jPkg);
				if (dir.exists() && dir.isDirectory())
					break;
			}
		}
		
		//we couldn't find the import directory...surely this is an error
		if (dir == null) {
			JavaStatic.runtime.error("Package could not be located for import (in JavaPackages.importMany): " + pkg);
			JavaStatic.runtime.exit(); //abort, we can't possibly go any further
		}
		
		//glob for our .java files to import
		File[] jFiles = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.indexOf(".java") > -1;
			}
		});
		
		//if we don't have any files...w/e, just return
		if (jFiles == null)
			return;
		
		//and import all our of files in this package
		for (File f : jFiles) {
			this.importFile(pkg + "." + f.getName().replace(".java", ""));
		}
	}
}
