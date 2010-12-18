package translator.Expressions;

import xtc.tree.GNode;

import translator.JavaType;
import translator.JavaClass;
import translator.JavaScope;

public class SuperExpression extends JavaExpression {

	public SuperExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}

	public void onInstantiate(GNode n) {
		this.setType(this.getJavaClass().getParent().getType());
	}

	public String print() {
		return this.getType().getJavaClass().getCppName(true, false);
	}
}

