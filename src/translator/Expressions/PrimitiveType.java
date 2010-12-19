package translator.Expressions;

import translator.JavaScope;
import translator.JavaType;
import xtc.tree.GNode;

/**
 * This clearly doesn't need to be in its own file....
 */
public class PrimitiveType extends JavaExpression {

	public PrimitiveType(JavaScope scope, GNode n) {
		super(scope, n);
		this.setType(JavaType.getType((String)n.get(0)));
	}

	public String print() {
		return this.getType().getCppName();
	}
}
