package translator.Expressions;

import translator.*;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

/**
 * Selections Expressions, hours of debugging fun free of charge!
 */
public class SelectionExpression extends Identifier {
	/**
	 * The JavaExpression making the selection.
	 */
	JavaExpression selector;

	/**
	 * The item that is being selected.
	 */
	JavaField selectee;

	public SelectionExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}
	
	protected void onInstantiate(GNode n) {
		this.selector = (JavaExpression)this.dispatch((GNode)n.get(0));
		this.selectee = this.selector.getField(n.get(1).toString());
		
		if (this.selectee == null) {
			JavaStatic.runtime.warning("Expressions.SelectionExpression: the selectee \"" + n.get(1).toString() + "\" could not be found in scope \"" + this.selector.getJavaClass().getName() + "\"");
		} else {
			this.fieldScope = ((JavaField)this.selectee).getType().getJavaClass();
			this.setType(this.selectee.getType());
		}
	}

	public String print() {
		return this.selector.print() + (this.selectee.isStatic() ? "::" : "-->") + this.selectee.getCppName(false);
	}
}
