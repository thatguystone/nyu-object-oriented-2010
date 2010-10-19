package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

class ExpnewClassExpression extends JavaExpression {
	
	String name;

	JavaClass cls;

	ExpnewClassExpression(JavaScope parent, Node n) {
		this.node = n;
		this.name = (String)n.get(2);
		this.setScope(parent);
		this.visit(this.node);
	}

	private void setType(String type) {	
		this.getFile().getImport(type).activate();
		this.cls = this.getFile().getImport(type);
	}

	public JavaFile getFile() {
		return this.getScope().getFile();
	}

	public String printMe() {
		String temp = "new " + this.getCppScope(this.getScope(), cls) + "(" + myExpressions.remove(0).printMe();
		for (Object o : myExpressions) {
			temp += ", " + ((JavaExpression)o).printMe();
		}
		return temp + ")";
	}
}
