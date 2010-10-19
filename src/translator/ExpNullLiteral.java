package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * I hated making a ThisExpression class,
 * but I hate this even more.
 */
class ExpNullLiteral extends JavaExpression {
	
	ExpNullLiteral(Node n) {
	}

	public String printMe() {
		return "null";
	}
}
