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
		this.visit(this.node);
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
		if (this.caller != null && this.caller.compareTo("this") != 0)
			//uses its own scope and the scope of the class its caller belongs to
			temp = temp + this.caller + "->" + getCppScope(this.getScope(), ((JavaField)this.getField(caller)).getCls());
		else
			temp = "__this->";
		if (myExpressions.size() > 0)
			temp = temp + this.getName() + "(" +  myExpressions.get(0).printMe();
		else
			temp = temp + " I'm Broken! ";
		for (int i = 1; i < myExpressions.size() ; i++)
			temp = temp + ", " + myExpressions.get(i).printMe();
		return temp;
	}
}
