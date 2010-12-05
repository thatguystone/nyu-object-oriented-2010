package translator.Expressions;

import translator.JavaScope;
import translator.JavaType;

import xtc.tree.GNode;

/**
 * A literal. Should probably merge StringLiteral with this class
 */
public class Literal extends JavaExpression {

	String value;

	//My God. A different constructor!!!!!!!!!
	public Literal(JavaScope scope, GNode n, JavaType type) {
		super(scope, n);
		this.setType(type);
	}
	
	protected void onInstantiate(GNode n) {
		this.value = (String)n.get(0);
	}

	public String print() {
		return value;
	}
}
