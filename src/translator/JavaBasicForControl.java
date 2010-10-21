package translator;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import xtc.tree.Node;
import xtc.tree.GNode;

class JavaBasicForControl extends ExpressionVisitor implements Nameable {

	JavaExpression type;

	String control;

	JavaBasicForControl (JavaScope scope, Node n) {
		this.setScope(scope);
		this.node = n;
		this.dispatch(n);
	}


	public String printMe() {
		return myExpressions.get(0).printMe() + ";" + myExpressions.get(1).printMe() + ";" + myExpressions.get(2).printMe();
	}
	
	/*public void visitDeclarators(GNode n) {
	
	}*/
	
	/*public void visitDeclarator(GNode n) {
			System.out.println("--------Did I Make It Here!!---------");
			type = myExpressions.get(0);
			System.out.println("000000000000000000000000000");
			JavaField field = new JavaField(this.myExpressions.get(0).printMe(), this.getScope(), this.getScope().getFile(), n);
	}*/
	
}
