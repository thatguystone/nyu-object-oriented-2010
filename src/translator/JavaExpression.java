package translator;

/**
 * Just to use as a type identifier
 */
abstract class JavaExpression extends ExpressionVisitor{
	/**
	 * The expression printer function
	 */
	abstract public String printMe();
}
