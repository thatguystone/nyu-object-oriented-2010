package translator.Statements;

import translator.JavaScope;
import translator.JavaStatic;
import translator.Printer.CodeBlock;
import translator.Expressions.JavaExpression;
import xtc.tree.GNode;

public abstract class JavaStatement extends JavaScope {
	/**
	 * Holds our node for printing.
	 */
	protected GNode node;
	
	/**
	 * Pass on our values to JavaScope, and save our node.
	 */
	public JavaStatement(JavaScope scope, GNode n){
		super(scope, n);
		this.node = n;
	}
	
	/**
	 * A generic way for statements to print their bodies without worrying about return type.
	 *
	 * @param b The code block to which to print the body.
	 * @param n The GNode that defines the body.
	 */
	protected void printBody(CodeBlock b, GNode n) {
		//we can go a few different routes with our body here:
		Object body = this.dispatch(n);
		//perhaps we have an expression
		if (body instanceof JavaExpression)
			b.pln(((JavaExpression)body).print());
		
		//might have been a block
		else if (body instanceof CodeBlock)
			b.attach((CodeBlock)body);
		
		else if (body instanceof JavaStatement)
			((JavaStatement)body).print(b);
		
		//we don't know what that was -- that's an error
		else
			JavaStatic.runtime.error("Statements.JavaStatement: Undefined body type: " + body.getClass().getName());
	}
	
	/**
	 * Print out the statement.
	 */
	public abstract void print(CodeBlock b);
}

