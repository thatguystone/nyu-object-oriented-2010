package translator;

import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import java.io.IOException;

public class CppWriter {
	private Writer writer;
	
	private static boolean nuked = false;
	private boolean writingFile = true;
	
	CppWriter(String fileName) {
		this(fileName, true);
	}
	
	CppWriter(String fileName, boolean append) {
		//attempt to write to the file -- if we fail, just write to stdout
		try {
			this.writer = new FileWriter(fileName, append);
		} catch (IOException e) {
			writingFile = false;
			this.writer = new BufferedWriter(new OutputStreamWriter(System.out));
		}
	}
	
	/**
	 * Each file is processed separately, but we only want to nuke our output file once.
	 */
	public static void nukeFile(String fileName) {
		if (!nuked) {
			nuked = true;
			
			File f = new File(fileName);
			if (f.exists() && f.canWrite() && !f.isDirectory())
				f.delete();
		}
	}
	
	public void write(String str) {
		try {
			this.writer.append(str);
		} catch (IOException e) { } //who cares?  Seems incredibly unlikely that an error will occur once the file is open.
	}
	
	public void writeln(String str) {
		this.write(str);
		this.linefeed();
	}
	
	public void linefeed() {
		this.write("\n");
	}
	
	public void flush() {
		try {
			this.writer.flush();
		} catch (IOException e) { }	
	}
	
	public void close() {
		try {
			//only close the stream if we are writing to a file
			if (this.writingFile)
				this.writer.close();
			else
				this.flush();
		} catch (IOException e) { }
	}
}
