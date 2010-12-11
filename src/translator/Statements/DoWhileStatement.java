package translator.Statements;

import java.util.ArrayList;

import translator.JavaScope;
import translator.JavaStatic;
import translator.JavaField;
import translator.FieldDec;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

public class DoWhileStatement extends JavaStatement {
	public DoWhileStatement (JavaScope scope, GNode n) {
		super(scope, n);
System.out.println("HELLO");
	}

	public void print(CodeBlock b) {
		b=b.block("do",true);
		printBody(b,(GNode)this.node.get(0));
		b.close(false).p("while ("+((JavaExpression)dispatch((GNode)this.node.get(1))).print()+");");
	}
}
