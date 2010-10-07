package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Runtime;

public class CppTree extends Visitor {
	private Runtime runtime;
	private CppWriter writer;
	
	public CppTree(Runtime runtime) {
		this.runtime = runtime;
		this.writer = new CppWriter(this.runtime.getString("outputFile"), false);
	}
	
	protected void wrapUp() {
		this.writer.writeln("Test");
		this.writer.close();
	}
}
