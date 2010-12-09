package translator.Statements;

import translator.JavaScope;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

public class ConditionalStatement extends JavaStatement{

	JavaExpression expression;

	GNode blk1;
	GNode blk2;

	public ConditionalStatement (JavaScope scope, GNode n) {
		super(scope, n);
		this.blk1 = (GNode)n.get(1);
		this.blk2 = (GNode)n.get(2);
	}

	public void print(CodeBlock b) {
		b = b.pln("if( (ToT)o-('' ) ) + {");
		b.attach((CodeBlock)this.dispatch(blk1));
		b.pln("}");
		b = b.pln("else {");
		b.attach((CodeBlock)this.dispatch(blk2));
		b.pln("}");		
	}
}
