package translator;

import java.util.ArrayList;
import java.util.*;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

class ExpCallExpression extends JavaExpression {

	/**
	 * Name of the variable calling this method, null means no caller or this
	 */
	String method = null;

	boolean hasCallee = false;

	ExpCallExpression(JavaScope parent, Node n){
		this.node = n;
		this.setScope(parent);
		this.setup();
		this.visit(this.node);
		//this.setCaller();
	}

	private void setup() {
		if (this.node.get(0) != null) {
			hasCallee = true;
			this.method = this.node.get(2).toString();
		}
		this.method = this.node.get(2).toString();
	}

	/**
	 * Does what is says.
	 */
	/*private void setCaller() {
		if (this.node.get(0) != null) {
			this.method = this.node.get(2).toString();
		}
	}*/

	private String getCaller() {
		return myExpressions.get(0).printMe().substring(myExpressions.get(0).printMe().lastIndexOf(':'));
	}

	/**
	 * Prints the C++ code
	 */
	public String printMe() {
		String temp = "";
		if (this.hasCallee) {
			if (this.myExpressions.get(0) instanceof ExpSelectionExpression)
				//uses its own scope and the scope of the class its caller belongs to
				temp = temp + myExpressions.get(0).printMe() /*getCppScope(this.getScope(), this.getCaller())*/ + "->" /*+ getCppScope(this.getScope(), this.getCaller())*/ + this.method + "(";
			else
				temp = temp + myExpressions.get(0).printMe() + "->" /*+ getCppScope(this.getScope(), ((JavaField)this.getField(caller.printMe())).getCls())*/ + this.method + "(";

			if (myExpressions.size() > 1)
				temp = temp + myExpressions.get(1).printMe();
			//else
				//temp = temp + "(I'm Broken!)";
			for (int i = 2; i < myExpressions.size() - 1; i++)
				temp = temp + ", " + myExpressions.get(i).printMe();
			return temp + ")";
		}
		else {
			temp = temp + "__this" + "->" + this.method + "(";
			if (myExpressions.size() > 0)
				temp = temp + "(" +  myExpressions.get(0).printMe();
			//else
				//temp = temp + "(I'm Broken!)";
			for (int i = 1; i < myExpressions.size() - 1; i++)
				temp = temp + ", " + myExpressions.get(i).printMe();
			return temp + ")";
		}
	}
}
