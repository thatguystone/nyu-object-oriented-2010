package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * A simple expression that appears in a code block.
 */
class JavaExpressionStatement extends ExpressionVisitor {

	JavaScope scope;

	JavaExpression expression;
	
	JavaExpressionStatement(JavaScope scope, Node n) {
		this.scope = scope;
		this.setScope(scope);
		this.node = n;
		this.dispatch(this.node);
		if (myExpressions.size() > 0)
			expression = (JavaExpression)myExpressions.get(0);
	}

	/**
	 * Print the expression.
	 */
	public String printMe() {
		if (myExpressions.size() > 0)
			return this.expression.printMe() + ";";
		return "ExpressionStatement not yet handled";
	}
}
