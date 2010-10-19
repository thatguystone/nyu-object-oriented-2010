package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * This is a non-assignment expression with one operator and two operands.
 * The operands are expressions themselves.
 */
class ExpTwoPartExp extends JavaExpression {
	
	String operator;

	JavaExpression first;

	JavaExpression second;

	ExpTwoPartExp(JavaScope parent, Node n) {
		this.node = n;
		this.setScope(parent);
		this.operator = (String)n.get(1);
		this.visit(this.node);
		if (myExpressions.size() > 0)
			this.first = (JavaExpression)this.myExpressions.get(0);
		if (myExpressions.size() > 1)
			this.second = (JavaExpression)this.myExpressions.get(1);
	}

	public String printMe() {
		if (myExpressions.size() > 1)
			return "(" + first.printMe() + this.operator + second.printMe() + ")";
		if (myExpressions.size() > 0)
			return "(" + first.printMe() + this.operator + " not yet handled " + ")";
		return "ExpTwoPartExp not yet handled";
	}
}
