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
public class UnaryExp extends JavaExpression {
	/**
	 * the operator
	 */
	String operator;

	/**
	 * the operand.
	 */
	JavaExpression operand;
	
	
	boolean order;
	
	public UnaryExp(JavaScope scope, GNode n) {
		super(scope, n);
	}
	protected void onInstantiate(GNode n) {
		if (n.get(0) instanceof String) {
			operator = (String)n.get(0);
			operand = (JavaExpression)this.dispatch((GNode)n.get(1));
			order = true;
		}	
		else {
			this.operand = (JavaExpression)this.dispatch((GNode)n.get(0));
			this.operator = (String)n.get(1);
			order = false;
		}
		this.setType(operand.getType());
	}
	public String print() {
		if (order)
			return "(" +  operator  + operand.print()+")";
		return "(" + operand.print() + operator + ")";
	}
}
