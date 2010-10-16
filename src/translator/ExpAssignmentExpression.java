package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

class ExpAssignmentExpression extends JavaExpression {

	String operator;

	JavaExpression first;

	JavaExpression second;

	ExpAssignmentExpression(Node n) {
		this.node = n;
		this.operator = (String)n.get(1);
		this.dispatch(this.node);
		this.first = (JavaExpression)this.myExpressions.get(0);
		this.second = (JavaExpression)this.myExpressions.get(1);
	}

	public String printMe() {
		return first.printMe() + this.operator + second.printMe();
	}
}
