package translator.Statements;

import translator.JavaScope;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

public class ForStatement extends JavaStatement{

	JavaExpression expression;

	GNode blk;

	public ForStatement (JavaScope scope, GNode n) {
		super(scope, n);
		this.blk = (GNode)n.get(1);
	}

	protected void onInstantiate(GNode n) {
	}

	public void print(CodeBlock b) {
		b = b.pln("for( :D :D :D ) + {");
		b.attach((CodeBlock)this.dispatch(blk));
		b.pln("}");
	}
}
