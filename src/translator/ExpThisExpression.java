package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * I REALLY can't believe I'm actually making this a class.... meh.
 * It holds a literal or a variable name, that's it.
 */
class ExpThisExpression extends JavaExpression {
	
	ExpThisExpression(Node n) {
	}

	public String printMe() {
		return "__this";
	}
}	