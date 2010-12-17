package translator.Expressions;

import xtc.tree.GNode;

import translator.JavaClass;
import translator.JavaScope;
import translator.JavaMethodSignature;
import translator.Expressions.JavaExpression;

/**
 * When a new class is declared.
 */
public class NewClassExpression extends JavaExpression {
	/**
	 * The class being instantiated.
	 */
	private JavaClass cls;
	
	/**
	 * The arguments.
	 */
	private JavaMethodSignature sig;
	
	/**
	 * If we have already printed.
	 */
	private boolean printed;
	
	public NewClassExpression(JavaScope scope, GNode n) {
		super(scope, n);
	}
	
	protected void onInstantiate(GNode n) {
		this.cls = this.getJavaFile().getImport(((Identifier)this.dispatch((GNode)n.get(2))).getRawValue());
		this.visit(n);
		this.setType(this.cls.getType());
		this.printed = false;
	}

	public String print() {
		//if we're a part of a chain and we've already printed, then we just need to output __chain
		if (this.partOfChain() && this.printed)
			return "__chain";

		this.printed = true;
	
		//cast every new class expression to a pointer, then the smart pointer can do all the extra casting for us
		String ret = "(" + this.cls.getCppName() + ")new " + this.cls.getCppName(true, false) + "(";
		
		//make our substringing easier -- only do it when we have args
		if (this.sig.size() > 0) {
			for (JavaScope s : this.sig.getArguments())
				ret += ((JavaExpression)s).print() + ", ";
			
			ret = ret.substring(0, ret.length() - 2);
		} 
		
		//since we're part of a chain, we need to set our return to the __chain guy
		if (this.partOfChain())
			return "(__chain = " + ret + "))";
		
		return "(" + ret + "))";
	}
	
	public void visitArguments(GNode n) {
		this.sig = new JavaMethodSignature();
		
		for (int i = 0; i < n.size(); i++) {
			JavaExpression e = (JavaExpression)this.dispatch((GNode)n.get(i));
			this.sig.add(e.getType(), e);
		}
	}
}
