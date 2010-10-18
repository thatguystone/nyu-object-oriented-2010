package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * This is a non-assignment expression with one operator and two operands.
 * The operands are expressions themselves.
 */
class ExpConditionalExpression extends JavaExpression {

	JavaExpression first;

	JavaExpression second;

	JavaExpression third;

	ExpConditionalExpression(JavaScope parent, Node n) {
		this.node = n;
		this.setScope(parent);
		this.dispatch(this.node);
		this.first = (JavaExpression)this.myExpressions.get(0);
		this.second = (JavaExpression)this.myExpressions.get(1);
		this.third = (JavaExpression)this.myExpressions.get(2);
	}

	public String printMe() {
		return "(" + first.printMe() + "?" + second.printMe() + ":" + third.printMe() + ")";
	}
}
