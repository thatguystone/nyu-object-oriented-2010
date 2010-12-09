package translator.Statements;

import translator.JavaScope;
import translator.JavaStatic;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

public class ConditionalStatement extends JavaStatement {
	public ConditionalStatement (JavaScope scope, GNode n) {
		super(scope, n);
	}

	public void print(CodeBlock b) {
		//get our java expression from the first (0) node
		JavaExpression e = ((JavaExpression)this.dispatch((GNode)this.node.get(0)));
		
		//send an error if the expression isn't implemented yet.
		if (e == null) {
			JavaStatic.runtime.error("Statements.ConditionalExpression: Expression type \"" + ((GNode)this.node.get(0)).getName() + "\" not found.");
			b = b.block("if ( EXPRESSION NOT FOUND )");
		} else {
			b = b.block("if (" + e.print() + ")");
		}
		
		//our body is the second (1) node
		this.printBody(b, (GNode)this.node.get(1));
		
		//do we have an "else"?
		//elses are the third (2) node
		if (this.node.get(2) != null) {
			//we have an else, so close our previous statement
			b = b.close(false);
		
			//let's determine the type of else (ie. is it "else" or "else if"?)
			GNode els = (GNode)this.node.get(2);
			
			//do we have an "else if"?
			if (els.getName().equals("ConditionalStatement")) {
				b = b.p("else ");
				
				//we don't have to close this statement -- there's another embedded if, so he does the closing
				((ConditionalStatement)this.dispatch(els)).print(b);
			} else {
				b = b.block("else");
				this.printBody(b, els);
				
				//only the else has to close himself -- he is the end, there are no other statements following
				//that close themselves.
				b.close();
			}
		
		//only close the block if we don't have an else
		} else {
			b.close();
		}
	}
}
