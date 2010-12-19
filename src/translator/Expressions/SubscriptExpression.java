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

	int dimensions;

	public SubscriptExpression(JavaScope scope, GNode n) {
		super(scope, n);
		this.dimensions = 1;
		this.setup(n);
	}

	public SubscriptExpression(JavaScope scope, GNode n, int dimensions) {
		super(scope, n);
		this.dimensions = dimensions + 1;
		this.setup(n);
	}

	protected void setup(GNode n) {
		this.accessor = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.setType(this.accessor.getType());
		if (this.accessor instanceof SubscriptExpression)
			this.setType(JavaType.getType(this.accessor.getType().getName()));
		this.value = (JavaExpression)this.dispatch((GNode)n.get(1));
	}

	public String print() {
		if (this.accessor instanceof SubscriptExpression)
			return this.accessor.print().substring(0, this.accessor.print().length() - 1) + ", " + this.value.print() + ")";
		
		String call = "(" + this.dimensions + ", " + this.value.print() + ")";
		
		//if we're accessing multiple dimensions
		if (this.accessor.getType().getDimensions() > this.dimensions)
			return "(ARRAY(" + this.accessor.getType().getCppName() + "))" + this.accessor.print() + "->getMulti" + call;
		
		return this.accessor.print() + "->get" + call;
	}

	public JavaExpression visitSubscriptExpression(GNode n) {
		return new SubscriptExpression(this, n, dimensions);
	}
}
