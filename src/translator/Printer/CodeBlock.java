package translator.Printer;

import java.lang.StringBuffer;

/**
 * A class that formats code blocks nicely and is capable of nesting.
 *
 * This class caches all of its output in a StringBuffer so that it doesn't print anything
 * when its methods are called.  Rather, it must be passed to CodePrinter, which will then
 * direct the code output to the proper location (stdout, a file, etc).
 */
public class CodeBlock {
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
	public int indent;
	
	/**
	 * If we're an empty, place-holding code block.
	 */
	private boolean empty = false;
	
	/**
	 * If we just have a block without a header
	 */
	public CodeBlock() {
		this.empty = true;
	}
	
	/**
	 * Constructor...
	 */
	public CodeBlock(String header) {
		this(null, header, true);
	}
	
	/**
	 * The constructor that indents blocks.  If we're given a parent, we match his indentation
	 * for our header, then we indent further.
	 */
	public CodeBlock(CodeBlock parent, String header, boolean withBrace) {
		if (parent != null) {
			this.parent = parent;
			this.indent = parent.indent;
		} else {
			this.indent = 0;
		}
		
		this.pln(header + (withBrace ? " {" : ""));
		this.indent++;
	}
	
	public CodeBlock(int indent) {
		this.empty = true;
		this.indent = indent - 1;
	}

	public CodeBlock block(String header) {
		return this.block(header, true);
	}
	
	public CodeBlock block(String header, boolean withBrace) {
		return new CodeBlock(this, header, withBrace);
	}

	/**
	 * Attach a CodeBlock to the current block.
	 */
	public CodeBlock attach(CodeBlock b) {
		if (b != null) {
			String tmp = b.toString();
			int i;
			
			//grab all the new lines and print them to our guy, with indentation
			while ((i = tmp.indexOf("\n")) > -1) {
				this.pln(tmp.substring(0, i));
				tmp = tmp.substring(i + 1);
			}
			
			//print the last line that is skipped in the while loop -- only if it's not blank
			if (tmp.length() > 0)
				this.pln(tmp);
		}
		
		return this;
	}
	
	public CodeBlock close() {
		return this.close(true);
	}
	
	/**
	 * When we're done with the block, be sure to close it.
	 */
	public CodeBlock close(boolean withSemicolon) {
		//are we not empty and therefore need to close ourself?
		if (!this.empty) {
			this.indent--;
			this.pln("}" + (withSemicolon ? ";\n" : ""));
		}
		
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
	 * Prints a string to the current block.
	 */
	public CodeBlock p(String text) {
		this.code.append(text);
		return this;
	}
	

	/**
	 * For printing out empty lines.
	 */
	public CodeBlock pln() {
		this.empty = false;
		return this.pln("");
	}
	
	/**
	 * Prints a line in the current block.
	 */
	public CodeBlock pln(String line) {
		if (!this.empty)
			this.indent();
		this.code.append(line + "\n");
		this.empty = false;
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
