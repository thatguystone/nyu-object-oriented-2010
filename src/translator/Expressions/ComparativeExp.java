package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import xtc.tree.GNode;

/**
 * An expression with two operands and an operator.
 */
public class ComparativeExp extends JavaExpression {

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

	public ComparativeExp(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		this.operator = (String)n.get(1);

		this.first = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.second = (JavaExpression)this.dispatch((GNode)n.get(2));

		//this.setType(JavaType.getType(null, "boolean"));
	}

	public String printMe() {
		return "(" + first.print() + operator + second.print() + ")";
	}

}
