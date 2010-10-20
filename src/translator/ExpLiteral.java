package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * I can't believe I'm actually making this a class.... meh.
 * It holds a literal or a variable name, that's it.
 */
class ExpLiteral extends JavaExpression {
	
	String value;
	
	ExpLiteral(Node n) {
		this.value = (String)n.get(0);
	}

	public String printMe() {
		return value;
	}		
}
