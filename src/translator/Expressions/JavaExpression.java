package translator.Expressions;

import translator.*;

import xtc.tree.Node;

/**
 * Just to use as a type identifier
 */
abstract public class JavaExpression extends ExpressionVisitor {
	
	/**
	 * The return type of this expression.
	 */
	private JavaType returnType;

	JavaExpression(JavaScope scope, Node n)	{
		super(scope, n);
	}

	/**
	 * Get the return type of this expression.
	 */
	public JavaType getReturnType() {
		return this.returnType;
	}

	/**
	 * Set the return type of this expression.
	 */
	public void setReturnType(JavaType type) {
		this.returnType = type;
	}

	/**
	 * A special method for selection expressions.
	 */
	public String getName() {
		JavaStatic.runtime.error("Just stop being bad. This method was called on an expression what wasn't a selection or identifier.");
		JavaStatic.runtime.exit();
		
		return null;
	}
}
