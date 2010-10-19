package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

class JavaReturnStatement extends ExpressionVisitor {
	
	JavaScope scope;

	JavaExpression expression;
	
	JavaReturnStatement(JavaScope scope, Node n) {
		this.scope = scope;
		this.node = n;
		this.dispatch(this.node);
		if (myExpressions.size() > 0)
			expression = (JavaExpression)myExpressions.get(0);
	}

	public String printMe() {
		if (myExpressions.size() > 0)
			return "return " + this.expression.printMe() + ";";
		return "ReturnStatement not yet handled";
	}

}
