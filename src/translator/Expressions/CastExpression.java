package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import xtc.tree.GNode;

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
		this.casted = this.dispatch((GNode)n.get(1));
		this.dispatch((GNode)n.get(0));
	}

	public void visitPrimitiveType(GNode n) {
		this.setType(JavaType.getType(null, (String)n.get(0)));
	}

	public void visitQualifiedIdentifier(GNode n) {
		String temp = "";
		for (Node g : n)
			temp += (String)g + ".";
		temp = temp.subString(0, temp.length() - 1);
		this.setType(JavaType.getType(this, temp));
	}

	public String printMe() {
		return "((" + this.getType().print() + ")" + this.casted.printMe() + ")";
	}
}
