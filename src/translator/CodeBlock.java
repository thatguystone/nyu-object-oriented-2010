package translator;

import java.lang.StringBuffer;

/**
 * A class that formats code blocks nicely and is capable of nesting.
 *
 * This class caches all of its output in a StringBuffer so that it doesn't print anything
 * when its methods are called.  Rather, it must be passed to CodePrinter, which will then
 * direct the code output to the proper location (stdout, a file, etc).
 */
class CodeBlock {
	/**
	 * The code that we are caching in this block.
	 */
	private StringBuffer code = new StringBuffer();
	
	/**
	 * The parent block, if there is one.
	 */
	private CodeBlock parent = null;
	
	/**
	 * The amount of indentation we have.
	 */
	private int indent = 0;
	
	/**
	 * Constructor...
	 */
	CodeBlock(String header) {
		this(null, header, true);
	}
	
	/**
	 * The constructor that indents blocks.  If we're given a parent, we match his indentation
	 * for our header, then we indent further.
	 */
	CodeBlock(CodeBlock parent, String header, boolean withBrace) {
		if (parent != null) {
			this.parent = parent;
			this.indent = parent.indent;
		}
		this.pln(header + (withBrace ? " {" : ""));
		this.indent++;
	}
	
	public CodeBlock block(String header) {
		return this.block(header, true);
	}
	
	public CodeBlock block(String header, boolean withBrace) {
		return new CodeBlock(this, header, withBrace);
	}
	
	public CodeBlock close() {
		return this.close(true);
	}
	
	/**
	 * When we're done with the block, be sure to close it.
	 */
	public CodeBlock close(boolean withSemicolon) {
		this.indent--;
		this.pln("}" + (withSemicolon ? ";" : "") + "\n");
		
		//if we have a parent, append our code to him
		if (this.parent != null)
			this.parent.code.append(this.toString());
		
		return this.parent;
	}
	
	/**
	 * Closes all the blocks from the current location.
	 */
	public void closeAll() {
		this.close();
		if (this.parent != null)
			this.parent.closeAll();
	}
	
	/**
	 * For printing out empty lines.
	 */
	public CodeBlock pln() {
		return this.pln("");
	}
	
	/**
	 * Prints a line in the current block.
	 */
	public CodeBlock pln(String line) {
		this.indent();
		this.code.append(line + "\n");
		return this;
	}
	
	/**
	 * Adds the current indentation level to the end of the string.
	 */
	private void indent() {
		int i = 0;
		while (i++ < this.indent)
			this.code.append("\t");
	}
	
	/**
	 * Dumps all of the code contained within to a string.
	 */
	public String toString() {
		return this.code.toString();
	}
}
