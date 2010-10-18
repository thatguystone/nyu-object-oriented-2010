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
	 * The C++ header prototype and typedef print queue.
	 */
	protected static ArrayList<CodeBlock> hProtoQueue;

	/**
	 * Instead of having blocks just print themselves, blocks will get added to the print queue
	 * and all blocks will be printed in one go.
	 * The C++ header print queue.
	 */
	protected static ArrayList<CodeBlock> hPrintQueue;

	/**
	 * Instead of having blocks just print themselves, blocks will get added to the print queue
	 * and all blocks will be printed in one go.
	 * The C++ cpp print queue.
	 */
	protected static ArrayList<CodeBlock> cppPrintQueue;

	/**
	 * A block that contains all the class prototypes and typedefs.
	 * Only one instance.
	 */
	protected CodeBlock protoBlock;

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
		return this.getScope().getScopeString() + "." + this.getScope().getName();
	}

	/**
	 * Gets the C++ namespace of a field declaration or method call.
	 * For example ClassB myB; in PackageA.ClassA with PackageB.ClassB
	 * will become PackageB::ClassB myB;.
	 */
	public String getCppScopeTypeless(JavaScope presentLocation, JavaScope refrencedLocation) {
		String scopeString = getCppScope(presentLocation, refrencedLocation);
		//rip off that last scope operator
		scopeString = scopeString.substring(0, scopeString.lastIndexOf("::") == -1 ? 0 : scopeString.lastIndexOf("::"));
		//and the next one, but this time with the class name as well
		scopeString = scopeString.substring(0, scopeString.lastIndexOf("::") == -1 ? 0 : scopeString.lastIndexOf("::"));

		if (scopeString.compareTo("") == 0)
			return "";
		//now lets re-add that last scope operator... I wonder if any extra work got done here?
		return scopeString + "::";
	}

	/**
	 * Gets the C++ namespace of a field declaration or method call with the class name.
	 * For example ClassB myB; in PackageA.ClassA with PackageB.ClassB
	 * will become PackageB::ClassB myB;.
	 */
	public String getCppScope(JavaScope presentLocation, JavaScope refrencedLocation) {
		String presentPath = presentLocation.getScopeString();
		String refPath = refrencedLocation.getScopeString();

		//god I hate Strings
		//anyway this removes the parts of presentPath and refPath that are the same
		while (presentPath.compareTo("") != 0 && refPath.compareTo("") != 0) {
			if ( 0 != presentPath.substring(0, (presentPath.indexOf('.') == -1 ? presentPath.length() : presentPath.indexOf('.')))
				.compareTo(refPath.substring(0, (refPath.indexOf('.') == -1 ? refPath.length() : refPath.indexOf('.')))))
				break;
			if (presentPath.indexOf('.') == -1)
				presentPath = "";
			else
				presentPath = presentPath.substring(presentPath.indexOf('.') + 1, presentPath.length());
			if (refPath.indexOf('.') == -1)
				refPath = "";
			else
				refPath = refPath.substring(refPath.indexOf('.') + 1, refPath.length());
		}

		//omfg why doesn't replace let me replace chars with strings
		//turn ref path into C++ code
		while (refPath.indexOf('.') != -1) {
			String firstHalf = refPath.substring(0, refPath.indexOf('.'));
			String secondHalf = refPath.substring(refPath.indexOf('.') + 1, refPath.length());
			refPath = firstHalf + "::" + secondHalf;	
		}

		if (refPath.compareTo("") == 0)
			return "";
		return refPath + "::";
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
	 * Check if we have this field.
	 * Objects that have populated field lists must implement this.
	 */
	public boolean hasField(String field) {
		return false;
	}

	/**
	 * Get the field from the field list. 
	 * If we don't have the field, ask our parent scope.
	 * This should never run all the way to the top because we're only translating working java.
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
		
		//printing moved to printAll()
		//and instruct our output to...well, output
		//JavaStatic.h.print(this.hBlock);
		//JavaStatic.cpp.print(this.cppBlock);
	}

	/**
	 * Print everything out after all code blocks have been constructed
	 * and populated with C++ code.
 	 */
	public static void printAll() {
		for (CodeBlock block : hProtoQueue)
			JavaStatic.h.print(block);
		for (CodeBlock block : hPrintQueue)
			JavaStatic.h.print(block);
		for (CodeBlock block : cppPrintQueue)
			JavaStatic.cpp.print(block);
	}
	
	/**
	 * Get the prototype block
	 */
	protected final CodeBlock getProto() {
		return this.protoBlock;
	}

	/**
	 * Sets up a cpp block for printing to the header.
	 *
	 * This function is provided here, rather than in a constructor, because you need
	 * to know the header information before you begin to print.  That is, you need
	 * to know what goes on the line before the "{" before you can start a code block,
	 * so this just helps with that.  This also does some work in the background to make
	 * print seamless for you.
	 */
	protected final CodeBlock hBlock(String header) {
		//if (!(this.hBlock instanceof CodeBlock)) {
			//now done in registerPrint()
			//this.hBlock = new CodeBlock(header);
		//}
		return this.hBlock;
	}
	
	/**
	 * Sets up a cpp block for printing to the cpp file.
	 *
	 * This function is provided here, rather than in a constructor, because you need
	 * to know the header information before you begin to print.  That is, you need
	 * to know what goes on the line before the "{" before you can start a code block,
	 * so this just helps with that.  This also does some work in the background to make
	 * print seamless for you.
	 */
	protected final CodeBlock cppBlock(String header) {
		//if (!(this.cppBlock instanceof CodeBlock)) {
			//now done in registerPrint()
			//this.cppBlock = new CodeBlock(header);
		//}
		return this.cppBlock;
	}

	/**
	 * Add ourself to the print queue and setup our blocks
	 */
	protected final void registerPrint(String header) {
		//initialize lists if they don't exist
		if (!(hPrintQueue instanceof ArrayList)) {
			hProtoQueue = new ArrayList<CodeBlock>();
			hPrintQueue = new ArrayList<CodeBlock>();
			cppPrintQueue = new ArrayList<CodeBlock>();
		}
		setupProtoBlock(header);
		setupHeaderBlock(header);
		setupCppBlock(header);
		//cppPrintQueue.add(this.cppBlock);
	}

	/**
	 * Prepare the header block for use.
 	 */
	protected final void setupHeaderBlock(String header) {
		String namespace;
		/*if (header.compareTo("") != 0) {
			if (header.indexOf('.') != -1) {
				namespace = header.substring(0, header.indexOf('.'));
				header = header.substring(header.indexOf('.') + 1, header.length());
			}
			else {
				namespace = header;
				header = "";
			}
			this.hBlock = new CodeBlock("namespace " + namespace);
		}
		else
			this.hBlock = new CodeBlock("Something has gone wrong");
		hPrintQueue.add(this.hBlock);
		*/
		while (header.compareTo("") != 0) {
			if (header.indexOf('.') != -1) {
				namespace = header.substring(0, header.indexOf('.'));
				header = header.substring(header.indexOf('.') + 1, header.length());
			}
			else {
				namespace = header;
				header = "";
			}
			if (!(this.hBlock instanceof CodeBlock)) {
				this.hBlock = new CodeBlock("namespace " + namespace);
				hPrintQueue.add(this.hBlock);
			}
			else
				this.hBlock = this.hBlock.block("namespace " + namespace);
		}
		if (!(this.hBlock instanceof CodeBlock))
			this.hBlock = new CodeBlock("Something has gone wrong");
		//return this.hBlock;
	}

	/**
	 * Prepare the prototype block for use.
	 * Yes, this is very similar(identical) to the header block
	 * but I couldn't use instanceof on a temporary code block.
	 * May change in the future.
 	 */
	protected final void setupProtoBlock(String header) {
		String namespace;
		/*if (header.compareTo("") != 0) {
			if (header.indexOf('.') != -1) {
				namespace = header.substring(0, header.indexOf('.'));
				header = header.substring(header.indexOf('.') + 1, header.length());
			}
			else {
				namespace = header;
				header = "";
			}
			headerBlock = new CodeBlock("namespace " + namespace);
		}
		else
			headerBlock = new CodeBlock("Something has gone wrong");
		*/
		while (header.compareTo("") != 0) {
			if (header.indexOf('.') != -1) {
				namespace = header.substring(0, header.indexOf('.'));
				header = header.substring(header.indexOf('.') + 1, header.length());
			}
			else {
				namespace = header;
				header = "";
			}
			if (!(this.protoBlock instanceof CodeBlock)) {
				this.protoBlock = new CodeBlock("namespace " + namespace);
				hProtoQueue.add(this.protoBlock);
			}
			else
				this.protoBlock = this.protoBlock.block("namespace " + namespace);
		}
		if (!(this.protoBlock instanceof CodeBlock))
			this.protoBlock = new CodeBlock("Something has gone wrong");
		//return this.protoBlock;
	}

	/**
	 * ok... so all three of these methods are the same, I'll switch to the single method version later
	 * but for now I can just use a copy paste. I need to do other stuff anyway.
	 */
	protected final void setupCppBlock(String header) {
		String namespace;
		/*if (header.compareTo("") != 0) {
			if (header.indexOf('.') != -1) {
				namespace = header.substring(0, header.indexOf('.'));
				header = header.substring(header.indexOf('.') + 1, header.length());
			}
			else {
				namespace = header;
				header = "";
			}
			headerBlock = new CodeBlock("namespace " + namespace);
		}
		else
			headerBlock = new CodeBlock("Something has gone wrong");
		*/
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
