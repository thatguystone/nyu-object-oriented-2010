package translator;

import xtc.tree.Printer;

import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;


/**
 * Responsible for printing out pretty code.
 */
class CodePrinter {
	/**
	 * The printer we use to print out all our prettiness.
	 */
	private Printer printer;
	
	/**
	 * Sets up our printer. Attempts to get a file writer on outputFile, and if it fails, then it just
	 * defaults to writing to stdout.  If it succeeds, then the file is destroyed and all the output
	 * goes into that file.
	 *
	 * @param ext The ext that the file being written should have. Typically, this will be "h" or "cpp".
	 */
	CodePrinter(String ext) {
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
			writer = new BufferedWriter(new OutputStreamWriter(System.out));
		}
		
		//set our printer to our writer
		this.printer = new Printer(writer); 
	}
}
