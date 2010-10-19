package translator;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

/**
 * Not actually sure what we're doing with arrays atm
 */
class ExpnewArrayExpression extends JavaExpression {

	String type;

	ExpnewArrayExpression(JavaScope parent, Node n) {
		this.node = n;
		this.setScope(parent);
		type = (String)n.get(0);
		this.visit(this.node);
	}

	public void visit(GNode n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}

	public String printMe() {return null;};
}
