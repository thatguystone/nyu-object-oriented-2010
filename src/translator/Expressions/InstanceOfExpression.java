package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import translator.JavaStatic;

import xtc.tree.GNode;

/**
 * An instanceof test
 */
public class InstanceOfExpression extends JavaExpression {

	JavaExpression check;

	JavaType checkType;

	public InstanceOfExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		this.setType(JavaType.getType("boolean"));
		this.check = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.checkType = ((JavaExpression)this.dispatch((GNode)((GNode)n.get(1)).get(0))).getType();		
	}

	public String print() {
		return "(" + this.check.print() + "->__vptr->getClass(" + this.check.print() + ") == " + checkType.getCppName(false, false) + "::__class())";
	}	
}
