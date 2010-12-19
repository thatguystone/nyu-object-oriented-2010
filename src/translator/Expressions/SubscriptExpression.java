package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import translator.JavaStatic;

import xtc.tree.GNode;

/**
 * A subscript expression.
 */
public class SubscriptExpression extends JavaExpression {

	/**
	 * Expression accessing a subscript.
	 */
	JavaExpression accessor;

	/**
	 * Value of the subscript.
	 */
	JavaExpression value;

	public SubscriptExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		this.accessor = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.value = (JavaExpression)this.dispatch((GNode)n.get(1));
		this.setType(accessor.getType());
	}

	public String print() {
		return "";
	}

}
