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
		this.getMyMethod().hasChaining();
		
		String toCheck = this.check.print();
		
		return "((java::lang::Class)(__chain = " + toCheck + "->__vptr->getClass(" + toCheck+ ")))->__vptr->isInstance(__chain, (java::lang::__Object*)new " + checkType.getCppName(false, false) + "())"; 
	}	
}
