package translator.Expressions;

import translator.*;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

/**
 * Selections Expressions, hours of debugging fun free of charge!
 */
class SelectionExpression extends JavaExpression {

	/**
	 * The JavaExpression making the selection.
	 */
	JavaExpression selector = null;

	/**
	 * Name of the selectee.
	 */
	String selecteeName = "";

	/**
	 * The actual selectee.
	 */
	JavaScope selectee = null;

	public SelectionExpression(JavaScope scope, Node n) {
		super(scope, n);
	}

	/**
	 * This method will be called by the selection expression's child.
	 * Basically selection expressions are too stupid to resolve their own problems so they
	 * push their work onto their child... yea, a real role model.
	 */
	public String getName() {
		return this.selecteeName;
	}

	protected void onInstantiate(GNode n) {
		this.selector = this.dispatch((Node)n.get(0));
		if (this.getScope() instanceof SelectionExpression)
			this.selecteeName = ((SelectionExpression)this.getScope()).getName() + ".";
		this.selecteeName += (String)n.get(1);
		if (this.selector.getReturnType() == null) {
			//this.selectee = this.getFile().getImport(selector.getName() + selecteeName);
			//this.setReturnType((JavaClass)this.selectee);
		}		
		else {
			//this.selectee = this.selector.getReturnType().getField(selecteeName);
			//this.setReturnType(((JavaField)this.selectee).getType());
		}
		this.setType(((JavaExpression)this.getScope()).getType());
	}

	public String printMe() {
		
	}
}
