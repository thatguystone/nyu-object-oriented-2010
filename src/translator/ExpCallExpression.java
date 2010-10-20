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
	String methName = null;

	boolean hasCallee = false;

	boolean isStatic;

	boolean isVirtual;

	JavaExpression caller;

	JavaClass type;

	JavaMethod method;

	ExpCallExpression(JavaScope parent, Node n){
		this.node = n;
		this.setScope(parent);
		this.visit(this.node);
		this.setup();
		//this.visit(this.node);
		//this.getInfo();
	}

	private void setup() {
		this.methName = this.node.get(2).toString();
		if (this.node.get(0) != null) {
			hasCallee = true;
			caller = myExpressions.get(0);
			this.type = caller.getType();
			this.isStatic = caller.isStatic();
			//this.isVirtual = this.type.getMeth(methName).isVirtual();
			this.isVirtual = false;
		}
		else {
			this.type = this.getCls();
			this.isStatic = false;
			this.isVirtual = this.getCls().getMeth(methName).isVirtual();
		}
	}

	public JavaClass getType() {
		return this.type.getMeth(methName).getReturnType();
	}

	public boolean isStatic() {
		return false;
	}

	/**
	 * Does what is says.
	 */
	/*private void setCaller() {
		if (this.node.get(0) != null) {
			this.method = this.node.get(2).toString();
		}
	}*/

	/*private void getStuff() {
		myExpressions.get(0).getStuff(this);
	}*/

	/*private String getCaller() {
		return myExpressions.get(0).printMe().substring(myExpressions.get(0).printMe().lastIndexOf(':'));
	}*/
	/*
	public void getCaller(JavaExpression expression) {
		this.caller = expression.getCaller();
	}

	public void getType(JavaExpression expression) {
		this.cls = expression.getType();
	}
	*/
	/**
	 * Prints the C++ code
	 */
	public String printMe() {
		String temp = "";
		/*
		if (this.hasCallee) {
			if (this.myExpressions.get(0) instanceof ExpSelectionExpression)
				//uses its own scope and the scope of the class its caller belongs to
				temp = temp + myExpressions.get(0).printMe() /*getCppScope(this.getScope(), this.getCaller())*//* + "->" /*+ getCppScope(this.getScope(), this.getCaller())*//* + this.methName + "(";
			else*/
				/*temp = temp + myExpressions.get(0).printMe() + "->" /*+ getCppScope(this.getScope(), ((JavaField)this.getField(caller.printMe())).getCls())*//* + this.methName + "(";*/

			/*if (myExpressions.size() > 1)
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
		*/
		if (this.hasCallee)
			temp = temp + myExpressions.get(0).printMe();
		else
			temp = temp + "__this";
		if (this.isStatic)
			temp = temp + ".";
		else
			temp = temp + "->";
		if (this.isVirtual)
			temp = temp + "__vptr->";
		temp = temp + this.methName + "(";
		if (this.hasCallee) {
			if (myExpressions.size() > 1)
				temp = temp + myExpressions.get(1).printMe();
			//else
				//temp = temp + "(I'm Broken!)";
			for (int i = 2; i < myExpressions.size() - 1; i++)
				temp = temp + ", " + myExpressions.get(i).printMe();
			return temp + ")";
		}
		else {
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





