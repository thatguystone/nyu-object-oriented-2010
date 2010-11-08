package translator.Printer;

import translator.JavaStatic;
import xtc.Constants;
import xtc.tree.Printer;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Responsible for printing out pretty code.
 */
public class CodePrinter extends Printer {
	/**
	 * Java doesn't like it when two things share System.out for stream writing,
	 * or something like that (only one stream was printing), so use a static guy
	 * to share it if the file can't be found.
	 */
	private static OutputStreamWriter systemOutWriter;

	/**
	 * The full path to the file, including the name.
	 */
	private String filePath;
	
	/**
	 * The name of the file we are writing to.
	 */
	private String fileName;
	
	/**
	 * The list of all the blocks that still need to be printed here.  We must cache them so that we can get their orders right
	 * for printing -> the prototypes, headers, and includes need to come in a specific order in the file, so we can't just
	 * allow the blocks to print out as they come in.  This blows.
	 */
	private Hashtable<Integer, ArrayList<CodeBlock>> blocks = new Hashtable<Integer, ArrayList<CodeBlock>>(); 
	
	/**
	 * This is not too important, because we're extending printer, but only allow construct from the factory method.
	 */
	private CodePrinter(Writer w, String fileName) {
		super(w);
		this.filePath = fileName;
		this.fileName = new File(fileName).getName();
	}
	
	/**
	 * Setup all the instances and pointers to output files and printers that we need.
	 */
	public static void prepare() {
		//we'll need 1 header
		JavaStatic.h = CodePrinter.factory("h");
		
		//and 1 cpp file
		JavaStatic.cpp = CodePrinter.factory("cpp");
		JavaStatic.cpp.pln("#include \"" + JavaStatic.h.getBaseName() + "\"").pln();
	}
	
	/**
	 * We're done writing, so close down those files.
	 */
	public static void wrapUp() {
		JavaStatic.h.dump();
		JavaStatic.cpp.dump();
	}
	
	/**
	 * We're done, so get everything dumped to a file.
	 */
	private void dump() {
		for (int i = 0; i < PrintOrder.ORDINAL.ordinal(); i++) {
			if (this.blocks.containsKey(i)) {
				for (CodeBlock b : this.blocks.get(i)) {
					this.p(b);
				}
			}
		}
		
		this.flush().close();
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
		return new CodePrinter(writer, name);
	}
	
	public String getBaseName() {
		return this.fileName;
	}
	
	/**
	 * Given a block, adds it to the output.
	 */
	public void p(PrintOrder o, CodeBlock block) {
		//that would be embarassing to print out a null....
		if (block != null) {
			int ord = o.ordinal();
		
			if (!this.blocks.containsKey(ord))
				this.blocks.put(ord, new ArrayList<CodeBlock>());
			
			this.blocks.get(ord).add(block);
		}
	}
	
	/**
	 * Where the real printing action happens.
	 */
	private void p(CodeBlock b) {
		this.p(b.toString());
	}
}
