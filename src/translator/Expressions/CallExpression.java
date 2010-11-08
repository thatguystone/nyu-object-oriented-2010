package translator.Expressions;

import java.util.ArrayList;

import translator.JavaMethod;
import translator.JavaMethodSignature;
import translator.JavaScope;
import xtc.tree.GNode;

/**
 * Good times, good times.
 */
public class CallExpression extends JavaExpression {
	/**
	 * The object or class making this method call
	 */
	private JavaExpression caller = null;
	
	/**
	 * String holding the name of the method.
	 * This is not really useful and will be removed later (I hope)
	 */
	private String methodName;
	
	/**
	 * The signature of the method to call.
	 */
	private JavaMethodSignature sig = new JavaMethodSignature();
	
	/**
	 * The called method.
	 */
	JavaMethod method;
	
	/**
	 * Why java, why?! Just override constructors :(
	 */
	public CallExpression(JavaScope scope, GNode n) {
		super(scope, n);
		
		//setup the other stuff we need
		//we do this here and not in onInstantiate because, when onInstantiate is called,
		//this.sig is not yet instantiated...so we can't use it.
		//seriously, wtf, java?
		this.visit(n);
	}

	/**
	 * Populate our list of arguments and set our caller.
	 */
	public void onInstantiate(GNode n) {
		//we only need the method name to begin with
		this.methodName = n.get(2).toString();
		
		//our caller
		this.setupCaller((GNode)n.get(0));
		
		//remove our caller from the visitor since we had to do him manually 
		//(each n[0] could be any number of things, so visitors didnt work)
		n.set(0, null);
	}
	
	/**
	 * Find out who is calling us.
	 */
	private void setupCaller(GNode n) {
		if (n == null) {
			//our parent? I think this is what the code was attempting to do before
			this.caller = (JavaExpression)this.getScope();
		} else {
			this.caller = (JavaExpression)this.dispatch(n);
		}
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
	/**
	 * It's a little-known fact that arguments come in all shapes and sizes.  Quite painfully, I might add.
	 */
	public void visitArguments(GNode n) {
		for (int i = 0; i < n.size(); i++) {
			JavaExpression e = (JavaExpression)this.dispatch((GNode)n.get(i));
			this.sig.add(e.getType(), e);
		}
	}
}
