package translator;

import java.lang.Exception;
import java.lang.NoSuchFieldException;

/**
 * Just to use as a type identifier
 */
abstract class JavaExpression extends ExpressionVisitor {
	/**
	 * The expression printer function
	 */
	abstract public String printMe();

	public JavaClass getType() {
		//throw new Exception("I don't get types");
		return null;
	}

	public boolean isStatic() {
		//why am I getting called????
		System.out.println("effff");
		return false;
	}

	public JavaClass getMyType() {

		return null;
	}

	public boolean isClass() {
		return false;
	}

	/**
	 * Gets the actual scope containing this object, not the JavaScope.
	 */
	public JavaScope getScope() {
		return this.parentScope.getScope();
	}

}
