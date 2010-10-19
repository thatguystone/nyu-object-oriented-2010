package translator;

/**
 * Just to use as a type identifier
 */
abstract class JavaExpression extends ExpressionVisitor {
	/**
	 * The expression printer function
	 */
	abstract public String printMe();

	/**
	 * Gets the actual scope containing this object, not the JavaScope.
	 */
	public JavaScope getScope() {
		return this.parentScope.getScope();
	}
}
