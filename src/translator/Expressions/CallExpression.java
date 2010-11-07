package translator.Expressions;

import translator.*;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

/**
 * Good times, good times.
 */
class CallExpression extends JavaExpression {

	/**
	 * The object or class making this method call
	 */
	JavaExpression caller = null;

	/**
	 * A list of all the arguments of this method call
	 */
	ArrayList<JavaExpression> arguments = new ArrayList<JavaExpression>();

	/**
	 * The types arguments passed into the method.
	 */
	ArrayList<JavaType> argumentTypes = new ArrayList<JavaType>();

	/**
	 * String holding the name of the method.
	 * This is not really useful and will be removed later (I hope)
	 */
	String methodName;

	/**
	 * The called method.
	 */
	JavaMethod method;

	CallExpression(JavaScope scope, Node n) {
		super(scope, n);
		this.dispatch(n);
		//I would like to remove this in the future
		methodName = (String)n.get(1);
	}

	/**
	 * Setup our call expression
	 */
	protected void onInstantiate() {
		this.method = ((JavaClass)this.caller.getReturnType()).findMethod(methodName, argumentTypes);
		this.setReturnType(this.method.getReturnType());
	}

	/**
	 * Populate our list of arguments and set our caller.
	 */
	public void processExpression(JavaExpression expression) {
		if(this.caller == null)
			this.caller = expression;
		else {
			this.arguments.add(expression);
			this.argumentTypes.add(expression.getReturnType());
		}
	}

}
