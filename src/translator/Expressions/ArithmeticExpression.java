package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import translator.JavaStatic;
import translator.Statements.JavaStatement;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

/**
 * An expression with two operands and an operator.
 */
public class ArithmeticExpression extends JavaExpression {
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

	public ArithmeticExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		this.operator = (String)n.get(1);

		this.first = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.second = (JavaExpression)this.dispatch((GNode)n.get(2));
		//System.out.println(this.first.getType().getName() + " " + this.second.getType().getName());

		//set our type based on our two operands
		this.setType(JavaType.getType(first.getType(), second.getType()));
	}

	public String print() {
		return "(" + "(" + this.getType().getCppName() + ")(" + this.first.print() + " " + this.operator + " " + this.second.print() + "))";
	}
}
