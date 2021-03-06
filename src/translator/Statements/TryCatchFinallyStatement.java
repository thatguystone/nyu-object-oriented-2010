package translator.Statements;
import java.util.ArrayList;
import translator.FieldDec;
import translator.JavaField;
import translator.JavaScope;
import translator.JavaStatic;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

public class TryCatchFinallyStatement extends JavaStatement {
	public TryCatchFinallyStatement(JavaScope scope, GNode n) {
		super(scope, n);
	}
	public void print(CodeBlock b){
		CodeBlock fn;
		GNode f=(GNode)this.node.get(2);
		//catch clause
		GNode c=(GNode)this.node.get(1);
		GNode p=(GNode)c.get(0);
		//type of exception
		GNode qid=(GNode)((GNode)p.get(1)).get(0);
		//try block (block expected)

		if (f!=null){
			fn=visitBlock(f);
			b=b.block("try").attach(visitBlock((GNode)this.node.get(0))).attach(fn).close(false)
				.block("catch ("+"java::lang::"+qid.get((qid.size()-1))+" "+p.get(3)+")")
				.attach(visitBlock((GNode)c.get(1))).attach(fn)
				.close(false);
		}else{
			b=b.block("try").attach(visitBlock((GNode)this.node.get(0))).close(false)
				.block("catch ("+"java::lang::"+qid.get((qid.size()-1))+" "+p.get(3)+")")
				.attach(visitBlock((GNode)c.get(1)))
				.close(false);
		}
	}
}
