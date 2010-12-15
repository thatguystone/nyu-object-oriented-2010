package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import translator.JavaStatic;
import translator.Statements.JavaStatement;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

/**
 * An assignment expression.
 */
public class AssignmentExpression extends JavaExpression {
	/**
	 * The variable being assigned.
	 */
	JavaExpression first;

	/**
	 * The value being assigned.
	 */
	JavaExpression second;

	public AssignmentExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		this.first = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.second = (JavaExpression)this.dispatch((GNode)n.get(2));
		
		this.setType(first.getType());
	}

	public String print() {
		return this.first.print() + " = " + this.second.print();
	}
}
