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
	/*
	public void getStuff(JavaExpression expression) {
		expression.getCaller(this);
	}

	public JavaField getCaller() {
		if (this.getField((String)this.node.get(1)) != null)
			return this.getField((String)this.node.get(1));
		return this.expression.getCaller((String)this.node.get(1))

	}

	public JavaField getCaller(String caller) {
		this.getScope().getFile().getImport((String)this.node.get(1)).activate();
		return this.getScope().getFile().getImport((String)this.node.get(1)).getField(caller);

	}
	*/

	public JavaClass getType() {
		return this.expression.getMyType();
	}

	public JavaClass getMyType() {
		this.getCls().getFile().getImport((String)this.node.get(1)).activate();
		return this.getCls().getFile().getImport((String)this.node.get(1));
	}

	public boolean isStatic() {
		//if (this.getType().getField((String)this.node.get(1)) != null)
			//return this.getType().getField((String)this.node.get(1)).isStatic();
		return true;
	}

	public boolean isClass() {
		return true;
	}

	public String printMe() {
		if (myExpressions.size() > 0)
			return expression.printMe() + "::" + (String)this.node.get(1);
		return "I hate my life";
	}
}
