package translator;

import xtc.Constants;
import xtc.tree.Printer;

import java.io.File;
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
	 * This is not too important, because we're extending printer, but only allow construct from the factory method.
	 */
	private CodePrinter(Writer w, String fileName) {
		super(w);
		this.filePath = fileName;
		this.fileName = new File(fileName).getName();
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
	 * Prints a line to the beginning of the file. (Useful for #includes)
	 */
	public CodePrinter b_pln(String l) {
		long line = this.line();
		
		this.line(1);
		this.pln(l);
		this.line(line + 1);
		return this;
	}
	
	
	/**
	 * Given a block, adds it to the output.
	 */
	public void print(CodeBlock block) {
		//that would be embarassing to print out a null....
		if (block != null)
			this.p(block.toString());
	}
}
