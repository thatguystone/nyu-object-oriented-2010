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
		
		JavaStatic.runtime.warning("Expressions.ArithmeticExpression: There is no return type set for arithmetic expressions yet.");
		//this.setType(JavaType.getType(first.getType(), second.getType()));
	}

	public String print() {
		//return second.print(first.print(b.p("(")).p(operator)).p(")");
		if (this.getScope() instanceof JavaStatement)
			return first.print() + " " + operator + " " + second.print();
		
		return "(" + first.print() + " " + operator + " " + second.print() + ")";
	}
}
