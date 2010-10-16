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

	ExpTwoPartExp(Node n) {
		this.node = n;
		this.operator = (String)n.get(1);
		this.dispatch(this.node);
		this.first = (JavaExpression)this.myExpressions.get(0);
		this.second = (JavaExpression)this.myExpressions.get(1);
	}

	public String printMe() {
		return "(" + first.printMe() + this.operator + second.printMe() + ")";
	}
}
