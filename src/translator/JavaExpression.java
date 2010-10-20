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
	
	/**
	 * Implement it yourself you lazy expression!
	 */
	/*
	public void getStuff(JavaExpression expression) {
		throw new Exception("Get your own stuff");
		return null;
	}
	
	public void getCaller(JavaExpression) {
		throw new Exception("Get your own stuff");
		return null;
	}

	public JavaField getCaller(String caller) {
		throw new NoSuchFieldException("Bitch, I don't get callers");
		return null
	}

	public JavaField getCaller(String caller) {
		throw new NoSuchFieldException("Bitch, I don't get callers");
		return null;
	}
	*/

	public JavaClass getType() {
		//throw new Exception("I don't get types");
		return null;
	}

	public boolean isStatic() {

		return false;
	}

	public JavaClass getMyType() {

		return null;
	}

	/**
	 * Gets the actual scope containing this object, not the JavaScope.
	 */
	public JavaScope getScope() {
		return this.parentScope.getScope();
	}

}
