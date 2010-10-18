package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

class ExpCallExpression extends JavaExpression {

	/**
	 * Name of the variable calling this method, null means no caller or this
	 */
	String caller = null;

	ExpCallExpression(JavaScope parent, Node n){
		this.node = n;
		this.setScope(parent);
		this.setCaller();
		this.dispatch(this.node);
	}

	/**
	 * Does what is says.
	 */
	private void setCaller() {
		if (this.node.get(0) != null) {
			if (((GNode)this.node.get(0)).get(0) != null)
				this.caller = (String)((GNode)this.node.get(0)).get(0);
		}
	}

	/**
	 * Prints the C++ code
	 */
	public String printMe() {
		String temp = "";
		if (this.caller != null)
			//uses its own scope and the scope of the class its caller belongs to
			temp = temp + this.caller + "->" + getCppScope(this.getScope(), ((JavaField)this.getField(caller)).getCls());
		else
			temp = "__this->";
		temp = temp + this.getName() + "(" +  myExpressions.remove(0).printMe();
		for (JavaExpression exp : myExpressions)
			temp = temp + ", " + myExpressions.remove(0).printMe();
		return temp;
	}
}
