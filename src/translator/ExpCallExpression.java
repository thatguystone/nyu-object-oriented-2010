package translator;

import java.util.ArrayList;
import java.util.*;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

class ExpCallExpression extends JavaExpression {
	String methName = null;

	boolean hasCallee = false;

	boolean isStatic;

	boolean isClass;

	boolean isVirtual;

	JavaExpression caller;

	boolean prepared = false;

	JavaClass type;

	JavaMethod method;

	ExpCallExpression(JavaScope parent, Node n){
		this.node = n;
		this.setScope(parent);
		this.visit(this.node);
	}

	private void preparePrint() {
		if (!(prepared)) {
			this.methName = this.node.get(2).toString();
			
			if (this.node.get(0) != null) {
				this.caller = myExpressions.get(0);
				this.type = caller.getType();
				
				this.hasCallee = true;
				
				this.isStatic = caller.isStatic();
				this.isClass = caller.isClass();
				
				//should never be null, but is.
				if (this.type.getMeth(methName) != null) {
					System.out.println("correct");
					this.isVirtual = this.type.getMeth(methName).isVirtual();
				} else {
					System.out.println(methName + " ---- broken");
					this.isVirtual = false;
				}
			}
			else {
				this.type = this.getCls();
				//this is just wrong.
				System.out.println("Who's here?" + methName);
				this.isStatic = false;
				this.isClass = false;
				this.isVirtual = this.type.getMeth(methName).isVirtual();
			}
		}
		this.prepared = true;
	}

	public JavaClass getType() {
		this.preparePrint();
		return this.type.getMeth(methName).getReturnType();
	}

	public boolean isStatic() {
		return false;
	}

	/**
	 * Prints the C++ code
	 */
	public String printMe() {
		this.preparePrint();
		String temp = "";

		if (this.hasCallee) {
			JavaExpression e = myExpressions.get(0);
			if (e instanceof ExpIdentifier)
				temp += ((ExpIdentifier)e).printMe(true);
			else
				temp += e.printMe();
		} else {
			temp += "__this";
		}
		
		if (this.type.getName().equals("java.lang.System"))
			temp += "->";
		else if (this.isClass && this.type.getField(this.methName) == null)
			temp += "::";
		else
			temp += "->";
		
		if (this.isVirtual)
			temp = temp + "__vptr->";
		
		temp = temp + this.methName + "(";
		if (this.hasCallee) {
			temp += (this.isStatic ? "" : this.caller.printMe());
			
			if (myExpressions.size() > 1)
				temp = temp + myExpressions.get(1).printMe();
			
			for (int i = 2; i < myExpressions.size() - 1; i++)
				temp = temp + ", " + myExpressions.get(i).printMe();
		} else {
			temp += (this.isStatic ? "" : "__this");
			
			if (myExpressions.size() > 0)
				temp = temp +  myExpressions.get(0).printMe();

			for (int i = 1; i < myExpressions.size() - 1; i++)
				temp = temp + ", " + myExpressions.get(i).printMe();
		}
		
		return temp + ")";
	}
}
