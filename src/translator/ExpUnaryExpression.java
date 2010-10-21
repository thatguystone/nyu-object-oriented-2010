package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * This is a non-assignment expression with one operator and two operands.
 * The operands are expressions themselves.
 */
class ExpUnaryExpression extends JavaExpression {
	
	String operator;

	JavaExpression first;

	ExpUnaryExpression(JavaScope parent, Node n) {
		this.node = n;
		this.setScope(parent);
		this.operator = (String)n.get(0);
		this.visit(this.node);
		if (myExpressions.size() > 0)
			this.first = (JavaExpression)this.myExpressions.get(0);
	}

	public String printMe() {
		if (myExpressions.size() > 0)
			return "(" + this.operator + first.printMe() + ")";
		return "ExpTwoPartExp not yet handled";
	}
}
