package translator;

import xtc.tree.Node;

class ExpSelectionExpression extends JavaExpression {

	JavaExpression expression;

	ExpSelectionExpression(JavaScope parent, Node n) {
		this.node = n;
		this.setScope(parent);
		this.visit(n);
		if (myExpressions.size() > 0)
			this.expression = myExpressions.get(0);
	}

	public String printMe() {
		if (myExpressions.size() > 0)
			return expression.printMe() + "::" + (String)this.node.get(1);
		return "I hate my life";
	}
}
