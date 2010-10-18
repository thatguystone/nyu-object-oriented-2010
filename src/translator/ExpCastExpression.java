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

	/**
	 * More primitives! I should make these global....
	 */
	private static ArrayList<String> primitives;

	ExpCastExpression(JavaScope parent, Node n) {
		setPrimitives();
		this.node = n;
		this.setScope(parent);
		this.setup();
		this.dispatch(this.node);
	}

	private static void setPrimitives() {
		if (!(primitives instanceof ArrayList)) {
			primitives = new ArrayList<String>();
			String[] p = {"byte", "short", "int", "long", "float", "double", "char", "boolean"};
			for(int i = 0; i < 8; i++)
				primitives.add(p[i]);
		}
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
			return "((" + this.getCppScope(this.getScope(), cls) + ")" + this.myExpressions.get(0).printMe() + ")";
		return "((" + cast + ")" + this.myExpressions.get(0).printMe() + ")";
	}

	public void visitDimensions(GNode n) {
		for (Object o : n) {
			dimensions++;
		}
	}
}
