package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

class ExpCallExpression extends JavaExpression {

	String caller = null;

	ExpCallExpression(JavaScope parent, Node n){
		this.node = n;
		this.setScope(parent);
		this.setCaller();
		this.dispatch(this.node);
	}

	private void setCaller() {
		if (this.node.get(0) != null) {
			if (((GNode)this.node.get(0)).get(0) != null)
				this.caller = (String)((GNode)this.node.get(0)).get(0);
		}
	}

	public String printMe() {
		String temp = "";
		if (this.caller != null) {
			temp = temp + getCppScope(this.getScope(), ((JavaField)this.getField(caller)).getCls());
		}
		temp = temp + this.getName() + "(" +  myExpressions.remove(0).printMe();
		for (JavaExpression exp : myExpressions)
			temp = temp + ", " + myExpressions.remove(0).printMe();
		return temp;
	}
}
