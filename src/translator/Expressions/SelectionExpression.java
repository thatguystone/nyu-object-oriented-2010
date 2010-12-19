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
		
		//make a first attempt to get the selectee
		this.selectee = this.selector.getField(n.get(1).toString());
		
		//if we didn't find a selectee, and our child couldn't find one...with our powers combined! we might be able to do something
		if (this.selectee == null && this.selector instanceof Identifier) {
			Identifier i = (Identifier)this.selector;
			
			//if of child, too, couldn't find a selectee
			if (i.valueError) {
				i = (Identifier)this.dispatch(GNode.create("PrimaryIdentifier", i.nodeValue + "." + n.get(1).toString()));
				
				//attempt to find a new value, but if we can't, then just return and let the parent handle it
				if (i.valueError) {
					this.valueError = true;
					this.nodeValue = i.nodeValue;
					return;
				}
				
				//we found some sort of value, so use that new value as our scope and selector
				this.fieldScope = i.fieldScope;
				this.selector = i;
			}
		}
		
		//attempt to get the field off of the new selector
		if (this.selectee == null) 
			this.selectee = this.selector.getField(n.get(1).toString());
		
		//if we still couldn't find the field from our new selector
		if (this.selectee == null) {
			//we didn't find a selectee -- since we're an identifier, set some value in Identifier so that our parent
			//knows that he should pull in our value and use that combined with his selectee value to attempt to find
			//what we are selecting.  This is for the: java.lang.System.out case.
			
			//set our type, just to keep things consistent
			this.setType(this.selector.getType(), true);
			
			//signal an error to our parent
			this.valueError = true;
			this.nodeValue = ((Identifier)this.selector).nodeValue;
		} else {
			this.valueError = false;
			this.fieldScope = this.selectee.getType().getJavaClass();
			this.setType(this.selectee.getType(), this.selectee.isStatic());
			
			this.isStaticAccess(this.selector.isTypeStatic() && this.selectee.isStatic());
		}
	}

	public String print() {
		String ret = "";
		
		if (this.valueError)
			return this.selector.print();
		
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
