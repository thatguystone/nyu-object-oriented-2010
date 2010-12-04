package translator.Statements;

import translator.JavaScope;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

public class WhileStatement extends JavaStatement{

	JavaExpression expression;

	GNode blk;

	public WhileStatement (JavaScope scope, GNode n) {
		super(scope, n);
		this.blk = (GNode)n.get(1);
	}

	protected void onInstantiate(GNode n) {
	}

	public void print(CodeBlock b) {
		b = b.pln("while( d(^O^)b ) + {");
		b.attach((CodeBlock)this.dispatch(blk));
		b.pln("}");
	}
}
