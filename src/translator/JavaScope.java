package translator;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.util.*;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

abstract class JavaScope extends Visitor {

	//not needed anymore
	//protected Hashtable<String, JavaVariable> variables = new Hashtable<String, JavaVariable>();

	/**
	 * Each scope is going to need a pointer to its file so that it can activate classes when
	 * they are run across.
	 */
	protected JavaFile file;

	/**
	 * Each scope needs a pointer to the JavaScope object it's contained in.
	 * This is NOT the scope containing the object.
	 */
	protected JavaScope parentScope;

	/**
	 * List of all fields in this class.
	 * Moved here for convenience.
	 * Field name -> Field Object
	 */
	protected LinkedHashMap<String, JavaField> fields = new LinkedHashMap<String, JavaField>();

	/**
	 * Instead of having blocks just print themselves, blocks will get added to the print queue
	 * and all blocks will be printed in one go.
	 * The C++ header print queue.
	 */
	protected static ArrayList<CodeBlock> hPrintQueue = new ArrayList<CodeBlock>();
	
	/**
	 * Print out all the vTables after we have the prototypes.
	 */
	protected static ArrayList<CodeBlock> vTablePrintQueue = new ArrayList<CodeBlock>();
	
	/**
	 * Print out all the vTables after we have the prototypes.
	 */
	protected static ArrayList<CodeBlock> prototypePrintQueue = new ArrayList<CodeBlock>();

	/**
	 * Instead of having blocks just print themselves, blocks will get added to the print queue
	 * and all blocks will be printed in one go.
	 * The C++ cpp print queue.
	 */
	protected static ArrayList<CodeBlock> cppPrintQueue = new ArrayList<CodeBlock>();
	
	/**
	 * This is used for testing if type is primitive
	 */
	protected static Hashtable<String, String> primitives = new Hashtable<String, String>(); 

	/**
	 * Our block printer to the C++ cpp file.
	 */
	protected CodeBlock cppBlock;
	
	/**
	 * Our block printer to the C++ header file.
	 */
	protected CodeBlock hBlock;
	
	/**
	 * Prints the information that this class needs to go to the header.
	 *
	 * This method should be overriden if you intend to put anything in the header
	 * file.
	 *
	 * When you override the method, you're going to want to make the following call
	 * to get you setup and going: CodeBlock block = this.hBlock("first line of block");
	 * Notice that you do not need to (nor should you) include the "{".
	 */
	protected void printHeader() { }
	
	/**
	 * Prints the information that this class needs to go to the implementation file.
	 *
	 * This method should be overriden if you intend to put anything in the implementation
	 * file.
	 *
	 * When you override the method, you're going to want to make the following call
	 * to get you setup and going: CodeBlock block = this.cppBlock("first line of block");
	 * Notice that you do not need to (nor should you) include the "{".
	 */
	protected void printImplementation() { }
	
	/**
	 * Sets the java file that contains whatever this is.
	 */
	public void setFile(JavaFile file) {
		this.file = file;
	}
	
	/**
	 * Gets the file that is the parent of this class.
	 */
	public JavaFile getFile() {
		return this.file;
	}

	/**
	 * Every scope(not JavaScope) must implement this to return something.
	 * Loops and such may simply return a number indicating which scope they are
	 * in a particular code block.
	 */
	public String getName() {
		return null;
	}

	/**
	 * Sets the parent scope of this JavaScope.
	 */
	public void setScope(JavaScope scope) {
		this.parentScope = scope;
	}

	public JavaClass getCls() {
		return this.getScope().getCls();
	}

	/**
	 * Gets the actual scope containing this object, not the JavaScope.
	 */
	public JavaScope getScope() {
		return this.parentScope;
	}
	/**
	 * Gets the scope represented as a string.
	 * For example java.land.String.length would be the scope of the method length
	 * in the class String in package java.lang.
	 */
	public String getScopeString() {
		return this.getScope().getScopeString();
	}
	
	protected static void setPrimitives() {
		if (primitives.size() == 0) {
			primitives.put("char", "char");
			primitives.put("byte", "int8_t");
			primitives.put("short", "int16_t");
			primitives.put("int", "int32_t");
			primitives.put("long", "int64_t");
			primitives.put("float", "float");
			primitives.put("double", "double");
			primitives.put("boolean", "bool");
			primitives.put("void", "void");
		}
	}

	/**
	 * Gets the C++ namespace of a field declaration or method call.
	 * For example ClassB myB; in PackageA.ClassA with PackageB.ClassB
	 * will become PackageB::ClassB myB;.
	 */
	public String getCppReferenceScope(JavaScope refrencedLocation) {
		String scopeString = "";
		String remaining = refrencedLocation.getScopeString();
		
		while (remaining.length() > 0) {
			int i = remaining.indexOf(".");
			
			if (i == -1) {
				scopeString += remaining;
				remaining = "";
			} else {
				scopeString += remaining.substring(0, i) + "::";
				remaining = remaining.substring(i + 1);
			}
		}
		
		return scopeString;
	}

	/**
	 * JavaField registers itself with its scope's field list because of the
	 * multiple declaration problem.
	 * All classes containing a field list must implement this.
	 */
	public void addField(JavaField field) {
		this.fields.put(field.getName(), field);
	}

	/**
	 * Check if this scope has this field.
	 */
	public boolean hasField(String field) {
		if (this.fields.containsKey(field))
			return true;
		return false;
	}

	/**
	 * Get the field from the field list. 
	 * If we don't have the field, ask our parent scope.
	 * Terminates at the class level.
	 * Returns null if the field is outside our scope hierarchy.
	 */
	public JavaField getField(String field) {
		if (!(this.hasField(field)))
			return this.getScope().getField(field);
		return this.fields.get(field);
	}

	/**
	 * Tells the class to go take itself out back and print itself.
	 */
	public final void triggerPrint() {
		//make the guy print himself
		this.printHeader();
		this.printImplementation();
	}

	/**
	 * Print everything out after all code blocks have been constructed
	 * and populated with C++ code.
 	 */
	public static void printAll() {
		for (CodeBlock block : prototypePrintQueue)
			JavaStatic.h.print(block);
		for (CodeBlock block : hPrintQueue)
			JavaStatic.h.print(block);
		for (CodeBlock leBlocky : vTablePrintQueue)
			JavaStatic.h.print(leBlocky);
		for (CodeBlock block : cppPrintQueue)
			JavaStatic.cpp.print(block);
	}
	
	/**
	 * Prepare the header block for use.
 	 */
	protected final CodeBlock setupBlock(ArrayList<CodeBlock> q, String namespace) {
		CodeBlock parentBlock = null;
		CodeBlock block = null;
		int index;
		
		do {
			index = namespace.indexOf('.');
			
			String name = "namespace ";
			if (index == -1)
				name += namespace;
			else
			 	name += namespace.substring(0, index);
			
			if (block == null)
				block = parentBlock = new CodeBlock(name);
			else
				block = block.block(name);
			
			namespace = namespace.substring(index + 1); 
		} while (index > 0);
		
		q.add(parentBlock);
		
		return block;
	}

	/**
	 * ok... so all three of these methods are the same, I'll switch to the single method version later
	 * but for now I can just use a copy paste. I need to do other stuff anyway.
	 */
	protected final void setupCppBlock(String header) {
		String namespace;
		while (header.compareTo("") != 0) {
			if (header.indexOf('.') != -1) {
				namespace = header.substring(0, header.indexOf('.'));
				header = header.substring(header.indexOf('.') + 1, header.length());
			}
			else {
				namespace = header;
				header = "";
			}
			if (!(this.cppBlock instanceof CodeBlock)) {
				this.cppBlock = new CodeBlock("namespace " + namespace);
				cppPrintQueue.add(this.cppBlock);
			}
			else
				this.cppBlock = this.cppBlock.block("namespace " + namespace);
		}
		if (!(this.cppBlock instanceof CodeBlock))
			this.cppBlock = new CodeBlock("Something has gone wrong");
		//return this.protoBlock;
	}
	
	public String printMe() {
		return null;	
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
	/**
	 * What it implies.
	 */
	public void visitFieldDeclaration(GNode n) {
		JavaFieldDec temp = new JavaFieldDec(this, this.getFile(), n);
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
