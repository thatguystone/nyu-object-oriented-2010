package translator;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import xtc.lang.JavaFiveParser;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;

/**
 * The public interface to our Java to C++ translator.
 */
public class Translator extends Tool {
	/**
	 * The file currently being processed.
	 */
	private String currentFile;
	
	/**
	 * Singleton so that only 1 can exist.
	 */
	private static Translator instance = null;
	
	/** Create a new translator. */
	private Translator() {
		JavaStatic.translator = this;
	}

	public String getCopy() {
		return "(C) 2010 5-Leaf Clover";
	}

	public String getName() {
		return "Java to C++ Translator";
	}

	public String getExplanation() {
		return "This tool translates a subset of Java to a subset of C++.";
	}

	public void init() {
		super.init();

		// Declare command line arguments.
		runtime
			.bool("printJavaAST", "printJavaAST", false, "Print the Java AST.")
			.word("outputFile", "outputFile", false, "The file to which to output the translated code (defaults to stdout; when the file cannot be written, goes to stdout)")
			.bool("debug", "debug", false, "Print debugging messages while translating.")
		;
	}
	
	public static Translator getInstance() {
		if (instance == null)
			instance = new Translator();
		
		return instance;
	}

	public void prepare() {
		super.prepare();
		
		JavaStatic.pkgs = JavaPackages.getInstance();
		JavaStatic.runtime = this.runtime;
	}
	
	public void wrapUp() {
		
	}

	public File locate(String name) throws IOException {
		File file = super.locate(name);

		if (Integer.MAX_VALUE < file.length())
			throw new IllegalArgumentException(file + ": file too large");

		return file;
	}

	public Node parse(Reader in, File file) throws IOException, ParseException {
		this.currentFile = file.toString();
		
		JavaFiveParser parser = new JavaFiveParser(in, file.toString(), (int)file.length());
		Result result = parser.pCompilationUnit(0);
		return (Node)parser.value(result);
	}
	
	public void process(Node node) {
		if (runtime.test("printJavaAST"))
			runtime.console().format(node).pln().flush();
		
		JavaFile f = new JavaFile(this.currentFile, node);
	}
	
	public static void main(String args[]) {
		Translator.getInstance().run(args);
	}
}

