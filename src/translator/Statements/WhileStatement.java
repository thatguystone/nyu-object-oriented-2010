package translator.Statements;

import translator.JavaScope;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

public class WhileStatement extends JavaStatement {

	JavaExpression expression;

	GNode blk;

	public WhileStatement (JavaScope scope, GNode n) {
		super(scope, n);
		this.blk = (GNode)n.get(1);
	}

	public void print(CodeBlock b) {
		b
			.block("while(" + ((JavaExpression)this.dispatch((GNode)this.node.get(0))).print() + ")")
				.attach((CodeBlock)this.dispatch(blk))
			.close()
		;
	}
}
