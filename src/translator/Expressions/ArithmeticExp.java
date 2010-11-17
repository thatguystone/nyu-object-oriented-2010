package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import translator.JavaStatement;

import xtc.tree.GNode;

/**
 * An expression with two operands and an operator.
 */
public class ArithmeticExp extends JavaExpression {
	/**
	 * The operator being used here.
	 */
	String operator;

	/**
	 * the first operand.
	 */
	JavaExpression first;

	/**
	 * The second operand.
	 */
	JavaExpression second;

	public ArithmeticExp(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		this.operator = (String)n.get(1);

		this.first = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.second = (JavaExpression)this.dispatch((GNode)n.get(2));

		//this.setType(JavaType.getType(first.getType(), second.getType()));
	}

	public String printMe() {
		String temp = first.printMe() + operator + second.printMe();
		if (this.getScope() instanceof JavaStatement)
			return temp;
		return "(" + temp + ")";
	}
}
