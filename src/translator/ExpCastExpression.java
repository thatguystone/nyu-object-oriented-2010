package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

class ExpCastExpression extends JavaExpression {

	String cast;

	boolean isClassCast = false;

	JavaClass cls;

	int dimensions = 0;

	ExpCastExpression(JavaScope parent, Node n) {
		setPrimitives();
		this.node = n;
		this.setScope(parent);
		this.setup();
		this.visit(this.node);
	}

	public JavaClass getType() {
		return this.cls;
	}

	public boolean isStatic() {
		return this.myExpressions.get(0).isStatic();
	}

	private void setup() {
		this.cast = (String)((GNode)((GNode)this.node.get(0)).get(0)).get(0);
		if (!(primitives.contains(this.cast))) {
			this.getScope().getFile().getImport(this.cast).activate();
			this.cls = this.getScope().getFile().getImport(this.cast);
			this.isClassCast = true;
		}
	}

	public String printMe() {
		if (this.isClassCast)
			return "((" + this.getCppReferenceScope(cls) + ")" + this.myExpressions.get(0).printMe() + ")";
		return "((" + cast + ")" + this.myExpressions.get(0).printMe() + ")";
	}

	public void visitDimensions(GNode n) {
		for (Object o : n) {
			dimensions++;
		}
	}
}
