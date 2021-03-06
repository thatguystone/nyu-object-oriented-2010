package translator.Statements;

import translator.JavaScope;
import translator.JavaStatic;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

public class ExpressionStatement extends JavaStatement {
	public ExpressionStatement (JavaScope scope, GNode n) {
		super(scope, n);
	}

	public void print(CodeBlock b) {
		JavaExpression e = (JavaExpression)this.dispatch((GNode)this.node.get(0));
		if (e != null) {
			b.pln(e.print() + ";");
		} else {
			JavaStatic.runtime.error("Statements.ExpressionStatement: No expression was found inside the expression statement.");
			b.pln("NO EXPRESSION;");
		}
	}
}
