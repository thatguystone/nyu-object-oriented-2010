package translator.Expressions;

import translator.*;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

/**
 * Selections Expressions, hours of debugging fun free of charge!
 */
public class SelectionExpression extends JavaExpression {

	/**
	 * The JavaExpression making the selection.
	 */
	JavaExpression selector;

	/**
	 * Name of the selectee.
	 */
	String selecteeName = "";

	/**
	 * The actual selectee.
	 */
	JavaScope selectee = null;

	public SelectionExpression(JavaScope scope, GNode n) {
		super(scope, n);
		this.selecteeName = (String)n.get(1);
		this.selector = (JavaExpression)this.dispatch((Node)n.get(0));
	}

	/**
	 * My parent in a selection expression, ono!
	 * Luckily I am too, so I'll be pushing my parent's work(and my own) onto my kid! Yay!
	 */
	public SelectionExpression(JavaScope scope, GNode n, String info) {
		//selecteeName = "." + info;
		super(scope, n);
		this.selecteeName = (String)n.get(1) + "." + info;
		this.selector = (JavaExpression)this.dispatch((Node)n.get(0));
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
	}

	public String print() {
		return selector.print();
	}

	/**
	 * Selection expressions don't always know their return type.
	 */
	public JavaType getType() {
		if(this.returnType != null)
			return this.returnType;
		return this.selector.getType();
	}

	/**
	 * ==================================================================================================
	 * Special Visitors for a special expression.
	 */

	public JavaExpression visitCallExpression(GNode n) {
		return new CallExpression(this, n, selecteeName);
	}
	
	public JavaExpression visitPrimaryIdentifier(GNode n) {
		return new Identifier(this, n, selecteeName);
	}

	public JavaExpression visitSelectionExpression(GNode n) {
		return new SelectionExpression(this, n, selecteeName);
	}


}
