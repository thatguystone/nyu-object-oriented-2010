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
	}

	protected void onInstantiate(GNode n) {
		if (((GNode)n.get(1)) != null)
			this.castTo = (JavaExpression)this.dispatch((GNode)((GNode)n.get(0)).get(0));
		else
			this.castTo = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.casted = (JavaExpression)this.dispatch((GNode)n.get(1));
		if (this.casted == null)
			this.casted = (JavaExpression)this.dispatch((GNode)n.get(2));
		this.setType(this.castTo.getType());
	}

	public String print() {
		String cast;
		if (this.castTo instanceof PrimitiveType)
			cast = "(" + this.castTo.print() + ")";
		else
			cast = "__rt::javaCast<" + this.castTo.print(true) + ">"; 
	
		return "(" + cast + "(" + this.casted.print() + "))";
	}
}
