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
	protected JavaExpression selector;

	/**
	 * The item that is being selected.
	 */
	protected JavaField selectee;
	
	/**
	 * If our selector is a "this" expression.
	 */
	private boolean selectorIsThis;

	public SelectionExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}
	
	protected void onInstantiate(GNode n) {
		this.selectorIsThis = false;
		this.selector = (JavaExpression)this.dispatch((GNode)n.get(0));
		
		this.selectee = this.selector.getField(n.get(1).toString());
		
		if (this.selectee == null) {
			//we didn't find a selectee -- since we're an identifier, set some value in Identifier so that our parent
			//knows that he should pull in our value and use that combined with his selectee value to attempt to find
			//what we are selecting.  This is for the: java.lang.System.out case.
			JavaStatic.runtime.warning(
				"Expressions.SelectionExpression: the selectee \"" + n.get(1).toString() + "\" could not be found in scope \"" + 
				this.selector.getJavaClass().getName() + "\""
			);
		} else {
			this.fieldScope = this.selectee.getType().getJavaClass();
			this.setType(this.selectee.getType(), this.selectee.isStatic());
			
			this.isStaticAccess(this.selector.isTypeStatic() && this.selectee.isStatic());
		}
	}

	public String print() {
		String ret = "";
		
		if (!this.selectorIsThis && this.isStaticAccess() && this.selector.isTypeStatic() && this.selectee.isStatic()) {
			ret += this.selector.print() + "::" + this.selectee.getTypedefName();
		} else if (!this.selectorIsThis && !this.selector.isTypeStatic() && this.selectee.isStatic()) {
			this.selector.isStaticAccess(true);
			ret += this.selector.print() + "::" + this.selectee.getTypedefName();
		} else {
			if (this.selectorIsThis && this.selectee.isStatic())
				ret += this.selectee.getJavaClass().getCppName(true, false);
			else if (this.selectorIsThis)
				ret += "__this";
			else
				ret += this.selector.print();
		
			ret += (this.selectee.isStatic() ? "::" : "->") + this.selectee.getCppName(false, !this.isStaticSet());
		}
		
		return ret;
	}
	
	/**
	 * A special case to handle a "this" selector.
	 */
	public JavaExpression visitThisExpression(GNode n) {
		this.selectorIsThis = true;
		return this;
	}
}
