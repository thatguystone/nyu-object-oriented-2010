package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Runtime;

/**
 * This just made me laugh, so I left it here.
 */
class MethodCounter extends Visitor {
	int count = 0;
	
	private Runtime runtime;
	
	MethodCounter(Runtime runtime) {
		this.runtime = runtime;
	}
	
	public void visitCompilationUnit(GNode n) {
		visit(n);
		this.runtime.console().p("Number of methods: ").p(count).pln().flush();
	}

	public void visitMethodDeclaration(GNode n) {
		count++;
		visit(n);
	}

	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				dispatch((Node)o);
		}
	}
}
