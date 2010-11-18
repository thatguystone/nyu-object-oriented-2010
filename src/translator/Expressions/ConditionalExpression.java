package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import xtc.tree.GNode;

/**
 * A conditional expression.
 */
public class ConditionalExpression extends JavaExpression {

	/**
	 * The 3 expressions that make up this conditional expression
	 */
	JavaExpression[] expressions = new JavaExpression[3];

	public ConditionalExpression (JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		expressions[0] = (JavaExpression)this.dispatch((GNode)n.get(0));
		expressions[1] = (JavaExpression)this.dispatch((GNode)n.get(1));
		expressions[2] = (JavaExpression)this.dispatch((GNode)n.get(2));

		this.setType(expressions[1].getType());
	}

	public String printMe() {
		return "(" + expressions[0].printMe() + "?" + expressions[1].printMe() + ":" + expressions[2].printMe() + ")";
	}

}
