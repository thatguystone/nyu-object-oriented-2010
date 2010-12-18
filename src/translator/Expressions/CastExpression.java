package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import translator.JavaStatic;
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
		JavaStatic.dumpNode(n);
		System.out.println(this.castTo.print());
	}

	protected void onInstantiate(GNode n) {
		this.castTo = (JavaExpression)this.dispatch((GNode)((GNode)(GNode)n.get(0)).get(0));
		this.casted = (JavaExpression)this.dispatch((GNode)n.get(1));
		this.setType(this.castTo.getType());
	}

	public String print() {
		return "__rt::javaCast<" + this.castTo.print(true) + ">(" + this.casted.print() + ")";
	}
}
