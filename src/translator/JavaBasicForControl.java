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
}
