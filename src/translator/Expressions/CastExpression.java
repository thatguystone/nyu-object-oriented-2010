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
	 * The expression getting casted.
	 */
	JavaExpression casted;

	public CastExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		this.casted = (JavaExpression)this.dispatch((GNode)n.get(1));
		this.dispatch((GNode)n.get(0));
	}


	public String print() {
		return "((" /*+ this.getType().print()*/ /*print method not yet implemented*/ + ")" + this.casted.print() + ")";
	}
}
