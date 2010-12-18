package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import xtc.tree.GNode;
import xtc.tree.Node;

/**
 * A cast.
 */
public class CastExpression extends JavaExpression {
	/**
	 * What we are casting to.
	 */
	JavaExpression castTo;

	/**
	 * The expression getting casted.
	 */
	JavaExpression casted;

	public CastExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		this.castTo = (JavaExpression)this.dispatch((GNode)((GNode)(GNode)n.get(0)).get(0));
		this.casted = (JavaExpression)this.dispatch((GNode)n.get(1));
	}

	public String print() {
		return "__rt::javaCast<" + this.castTo.getJavaClass().getCppName(true, true) + ">(" + this.casted.print() + ")";
	}
}
