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
	String selecteeName;

	/**
	 * The actual selectee.
	 */
	JavaScope selectee = null;

	SelectionExpression(JavaScope scope, Node n) {
		super(scope, n);
		this.dispatch(n);
		this.selecteeName = (String)n.get(1);
	}

	public String getName() {
		return this.selector.getName() + selecteeName + ".";
	}

	protected void onInstantiate() {
		if(this.selector.getReturnType() == null) {
			//this.selectee = this.getFile().getImport(selector.getName() + selecteeName);
			//this.setReturnType((JavaClass)this.selectee);
		}		
		else {
			//this.selectee = this.selector.getReturnType().getField(selecteeName);
			//this.setReturnType(((JavaField)this.selectee).getType());
		}
	}

	/**
	 * Set our selector.
	 */
	public void processExpression(JavaExpression expression) {
		if(this.selector == null)
			this.selector = expression;
	}

	
}
