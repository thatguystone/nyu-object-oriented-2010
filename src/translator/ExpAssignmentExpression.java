package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

class ExpAssignmentExpression extends JavaExpression {

	String operator;

	JavaExpression first;

	JavaExpression second;

	ExpAssignmentExpression(JavaScope parent, Node n) {
		this.node = n;
		this.setScope(parent);
		this.operator = (String)n.get(1);
		this.visit(this.node);
		this.first = (JavaExpression)this.myExpressions.get(0);
		this.second = (JavaExpression)this.myExpressions.get(1);
		
	}

	public String printMe() {
		return first.printMe() + this.operator + second.printMe();
	}
}
