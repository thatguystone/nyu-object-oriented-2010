package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import translator.JavaStatic;
import translator.Statements.JavaStatement;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

/**
 * An expression with two operands and an operator.
 */
public class ArithmeticExpression extends JavaExpression {
	/**
	 * The operator being used here.
	 */
	String operator;

	/**
	 * the first operand.
	 */
	JavaExpression first;

	/**
	 * The second operand.
	 */
	JavaExpression second;

	public ArithmeticExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		this.operator = (String)n.get(1);

		this.first = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.second = (JavaExpression)this.dispatch((GNode)n.get(2));
		
		JavaStatic.runtime.warning("Expressions.ArithmeticExpression: There is no return type set for arithmetic expressions yet.");
		//this.setType(JavaType.getType(first.getType(), second.getType()));
	}

	public String print() {
		String cast = "";
		
		//if we have different types, do an explicit cast so that things will work
		if (this.first.getType() != this.second.getType()) {
			//----------------------------------------------------------------------------------------------------------
			//IS THIS NECESSARY FOR WHEN WE HAVE TWO CLASSES?  CAN'T THE SMART POINTER TAKE CARE OF IT?
			//----------------------------------------------------------------------------------------------------------
			
			JavaStatic.runtime.warning("Expressions.ArithmeticExpression: Operating with two expressions of different types, we need a cast, but we don't have arithmetic types yet.");
		}
	
		//return second.print(first.print(b.p("(")).p(operator)).p(")");
		if (this.getScope() instanceof JavaStatement)
			return this.first.print() + " " + this.operator + " " + cast + this.second.print();
		
		return "(" + this.first.print() + " " + this.operator + " " + cast + this.second.print() + ")";
	}
}
