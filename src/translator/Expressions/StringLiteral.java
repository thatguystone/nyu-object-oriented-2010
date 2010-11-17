package translator.Expressions;

import translator.JavaScope;
import translator.JavaType;

import xtc.tree.GNode;

/**
 * A string.  Yay.
 */
public class StringLiteral extends JavaExpression {
	String value;
	
	public StringLiteral(JavaScope s, GNode n) {
		super(s, n);
	}
	
	protected void onInstantiate(GNode n) {
		this.setType(JavaType.getType("java.lang.String"));
		this.value = n.get(0).toString();
	}

	public String printMe() {
		return value;
	}
}
