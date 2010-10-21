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
		this.name = ((GNode)n.get(2)).get(0).toString();
		this.setScope(parent);
		this.setType(this.name);
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
		String temp = "new " + this.getCppReferenceScope(cls, true) + "(";
		
		String args = "";
		if (myExpressions.size() > 1) {
			ArrayList<JavaExpression> list = (ArrayList<JavaExpression>)myExpressions.clone();
			list.remove(0);
			for (Object o : list) {
				args += "," + ((JavaExpression)o).printMe();
			}
			
			args = args.substring(1);
		}
		
		return temp + args + ")";
	}
}
