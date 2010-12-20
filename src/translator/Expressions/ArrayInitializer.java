package translator.Expressions;

import xtc.tree.GNode;
import xtc.tree.Node;

import translator.JavaStatic;
import translator.JavaClass;
import translator.JavaScope;
import translator.JavaType;

import java.util.ArrayList;

/**
 * When a new class is declared.
 */
public class ArrayInitializer extends JavaExpression {
	
	ArrayList<JavaExpression> expressions;

	int dimensions;

	public ArrayInitializer(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		expressions = new ArrayList<JavaExpression>();
		for (Object o : n) {
			if (o instanceof GNode) {
				JavaExpression e = (JavaExpression)this.dispatch((GNode)o);
				expressions.add(e);
				if (e.getType().getName().equals("null"));
				else {
					this.setType(JavaType.getType(e.getType().getName(), e.getType().getDimensions() + 1));
				}
			}
		}
		dimensions = expressions.size();
	}

	public String print() {
		String ret = "new java::util::__Array<" + this.getType().getCppName() + ">(java::util::__InitializerListTag::tag, " + this.dimensions + ", ";
		for (int i = 0; i < expressions.size(); i++)
			ret += expressions.get(i).print() + ", ";
		return ret.substring(0, ret.length() - 2) + ")";
	}
}

