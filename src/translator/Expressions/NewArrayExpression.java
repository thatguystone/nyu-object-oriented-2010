package translator.Expressions;

import xtc.tree.GNode;
import xtc.tree.Node;

import translator.JavaStatic;
import translator.JavaClass;
import translator.JavaScope;
import translator.JavaMethodSignature;
import translator.Expressions.JavaExpression;

/**
 * When a new class is declared.
 */
public class NewArrayExpression extends JavaExpression {

	int dimensions;

	private JavaMethodSignature sig;

	public NewArrayExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}

	protected void onInstantiate(GNode n) {
		JavaStatic.dumpNode(n);
		this.dimensions = 0;
		this.setType(((JavaExpression)this.dispatch((GNode)n.get(0))).getType());
		this.dispatch((GNode)n.get(1));
	}

	public String print() {
		String ret = "";
		ret += (this.getType() != null?this.getType().getCppName(true):"NULL TYPE") + "(" + dimensions + ", ";
		if (this.sig.size() > 0) {
			for (JavaScope s : this.sig.getArguments())
				ret += ((JavaExpression)s).print() + ", ";
		
			ret = ret.substring(0, ret.length() - 2);
		}
		return ret + ")";
	}

	public void visitConcreteDimensions(GNode n) {
		this.dimensionVisitor(n);
	}

	public void dimensionVisitor(GNode n) {
		this.sig = new JavaMethodSignature();
		for (Object o : n) {
			dimensions++;
			if (o instanceof Node) {
				JavaExpression e = (JavaExpression)this.dispatch((Node)o);
				if (e != null) {
					if (e.getType() == null)
						System.out.println(e.print());
					else
						this.sig.add(e.getType(), e);
				}
			}
		}
	}
}
