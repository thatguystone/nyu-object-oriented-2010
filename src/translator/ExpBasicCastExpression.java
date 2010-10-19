package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

class ExpBasicCastExpression extends JavaExpression {

	String cast;

	JavaClass cls;

	int dimensions = 0;

	ExpBasicCastExpression(JavaScope parent, Node n) {
		this.node = n;
		this.setScope(parent);
		this.cast = (String)((GNode)this.node.get(0)).get(0);
		this.visit(this.node);
	}

	public String printMe() {
		return "((" + cast + ")" + this.myExpressions.get(0).printMe() + ")";
	}

	public void visitDimensions(GNode n) {
		for (Object o : n) {
			dimensions++;
		}
	}
}
