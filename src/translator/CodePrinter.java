package translator;

import xtc.Constants;
import xtc.tree.Printer;

import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;


/**
 * Responsible for printing out pretty code.
 */
class CodePrinter extends Printer {
	/**
	 * Java doesn't like it when two things share Sysetm.out for stream writing,
	 * or something like that (only one stream was printing), so use a static guy
	 * to share it if the file can't be found.
	 */
	private static OutputStreamWriter systemOutWriter;

	/**
	 * This is not too important, because we're extending printer, but only allow construct from the factory method.
	 */
	private CodePrinter(Writer w) {
		super(w);
	}

	/**
	 * Sets up our printer. Attempts to get a file writer on outputFile, and if it fails, then it just
	 * defaults to writing to stdout.  If it succeeds, then the file is destroyed and all the output
	 * goes into that file.
	 *
	 * @param ext The ext that the file being written should have. Typically, this will be "h" or "cpp".
	 */
	public static CodePrinter factory(String ext) {
		String fileName = JavaStatic.runtime.getString("outputFile");
		String name = "";
		
		//attempt to find the proper name of the file we should be writing to
		if (fileName != null) {
			int extLoc = fileName.lastIndexOf("."); //see if we have an extension
			if (extLoc > -1) {
				//see if the extension is for a c++ or h file
				String fileExt = fileName.substring(extLoc + 1); 
				if (fileExt.equals("cpp") || fileExt.equals("cc") || fileExt.equals("h"))
					name = fileName.substring(0, extLoc) + "." + ext;
			}
		}
		
		//attempt to write to a file -- fail by writing to stdout
		Writer writer;
		try {
			writer = new FileWriter(name);
		} catch (IOException e) {
			if (!(systemOutWriter instanceof OutputStreamWriter))
				systemOutWriter = new OutputStreamWriter(System.out);
			
			writer = systemOutWriter;
		}
		
		//set our printer to our writer
		return new CodePrinter(writer);
	}
	
	/**
	 * The pln method in xtc.Printer is too difficult to use -- requires indentation before every line.  This automatically
	 * adds in the indentation before printing the line.
	 */
	public Printer pln(String s) {
		this.indent();
		super.pln(s);
		return this;
	}
	
	/**
	 * Indentation is, by default, not enough, so this doubles it (to 4 WHOLE spaces!)
	 */
	public Printer incr() {
		indent += 4;
		return this;
	}
	
	/**
	 * The counter-part of incr().
	 */
	public Printer decr() {
		indent -= 4;
		return this;
	}
	
	/**
	 * Creates a new indented block.
	 *
	 * @param header The header for the block (the "if (...)" part of an if statement)
	 */
	public CodeBlock block(String header) {
		return new CodeBlock(this, header);
	}
	
	/**
	 * Creates a nested CodeBlock.
	 */
	public CodeBlock block(CodeBlock block, String header) {
		return new CodeBlock(this, block, header);
	}
}

/**
 * A class that formats code blocks nicely and is capable of nesting.
 */
class CodeBlock {
	/**
	 * The Code Printer.
	 */
	private CodePrinter writer;
	
	/**
	 * The parent block, if there is one.
	 */
	private CodeBlock parent = null;
	
	/**
	 * Constructor...
	 */
	CodeBlock(CodePrinter writer, String header) {
		this.writer = writer;
		this.writer.pln(header + " {");
		this.writer.incr();
	}
	
	CodeBlock(CodePrinter writer, CodeBlock block, String header) {
		this(writer, header);
		this.parent = block;
	}
	
	public CodeBlock block(String header) {
		return this.writer.block(this, header);
	}
	
	/**
	 * When we're done with the block, be sure to close it.
	 */
	public CodeBlock close() {
		this.writer.decr();
		this.writer.pln("}\n");
		return this.parent;
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
		this.writer.pln(line);
		return this;
	}
}
