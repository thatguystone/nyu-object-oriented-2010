package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

class ExpnewClassExpression extends JavaExpression {
	
	String name;

	ExpnewClassExpression(Node n) {
		this.node = n;
		this.name = (String)n.get(2);
		this.dispatch(this.node);
	}

	public String printMe() {
		String temp = "new " + name + "(" + myExpressions.remove(0).printMe();
		for (Object o : myExpressions) {
			temp += ", " + ((JavaExpression)o).printMe();
		}
		return temp + ")";
	}
}
