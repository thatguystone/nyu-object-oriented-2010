package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import translator.Statements.JavaStatement;
import translator.Statements.JavaBasicForControl;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

/**
 * An expression with two operands and an operator.
 */
public class PostfixExp extends JavaExpression {
	/**
	 * the operator
	 */
	String operator;

	/**
	 * the operand.
	 */
	JavaExpression operand;
	public PostfixExp(JavaScope scope, GNode n) {
		super(scope, n);
	}
	protected void onInstantiate(GNode n) {
		this.operand = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.operator = (String)n.get(1);
		//this.setType(JavaType.getType(operand.getType());
	}
	public String print() {
//		if (this.getScope() instanceof JavaStatement||this.getScope() instanceof JavaBasicForControl)
//			return operand.print() + operator;
		return "(" + operand.print() + operator  + ")";
//		return "MATH";
	}
/*
	public JavaExpression visitPrimaryIdentifier(GNode n) {
		System.out.println(this.getScope());
		return new Identifier(this.getScope(), n);
	}
*/
}
