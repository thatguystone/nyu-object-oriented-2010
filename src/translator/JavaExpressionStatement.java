package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

class JavaExpressionStatement extends ExpressionVisitor {

	JavaScope scope;

	JavaExpression expression;
	
	JavaExpressionStatement(JavaScope scope, Node n) {
		this.scope = scope;
		this.node = n;
		this.dispatch(this.node);
		expression = (JavaExpression)myExpressions.remove(0);
	}

	public String printMe() {
		return this.expression.printMe() + ";";
	}
}
