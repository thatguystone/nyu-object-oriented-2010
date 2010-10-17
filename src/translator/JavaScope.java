package translator;

import java.util.Hashtable;
import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

abstract class JavaScope extends Visitor {
	protected Hashtable<String, JavaVariable> variables = new Hashtable<String, JavaVariable>();

	/**
	 * Each scope is going to need a pointer to its file so that it can activate classes when
	 * they are run across.
	 */
	protected JavaFile file;
	
	/**
	 * A block that contains all the class prototypes and typedefs.
	 * Only one instance.
	 */
	protected CodeBlock protoBlock;

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
	 * JavaField registers itself with its scope's field list because of the
	 * multiple declaration problem.
	 * All classes containing a field list must implement this.
	 */
	public void addField(JavaField field) {

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

	public static void printAll() {
		//JavaStatic.h.print(protoBlock);
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
	protected final CodeBlock getProto(JavaScope scope) {
		return protoBlock;
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
		if (!(this.hBlock instanceof CodeBlock)) {
			//now done in registerPrint()
			//this.hBlock = new CodeBlock(header);
		}
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
		if (!(this.cppBlock instanceof CodeBlock)) {
			//now done in registerPrint()
			//this.cppBlock = new CodeBlock(header);
		}
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
		this.protoBlock = new CodeBlock(header);
		this.hBlock = new CodeBlock(header);
		this.cppBlock = new CodeBlock(header);
		hProtoQueue.add(this.protoBlock);
		hPrintQueue.add(this.hBlock);
		cppPrintQueue.add(this.cppBlock);
	}
	
	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
	/**
	 * What it implies.
	 */
	public void visitFieldDeclaration(GNode n) {
		JavaFieldDec temp = new JavaFieldDec(this, n);
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
