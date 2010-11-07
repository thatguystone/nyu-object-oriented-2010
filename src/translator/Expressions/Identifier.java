package translator.Expressions;

import translator.*;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * It holds a name, that's it.
 */
class Identifier extends JavaExpression {
	
	/**
	 * The identifier
	 */
	String Id;

	/**
	 * Yea, I'm not sure what to call it, and even then this is a bad name, oh well.
	 * This is whatever Id refers to
	 */
	JavaScope something;

	/**
	 * The names... they're just so good
	 * Package = 0, Class = 1, Field = 2
	 */
	int 	whatAmI = 0;

	Identifier(JavaScope scope, Node n) {
		super(scope, n);
		this.Id = (String)n.get(0);
	}

	public String getName() {
		return Id + ".";
	}

	/**
	 * Setup return type and figure out what this idendifier is identifiying.
	 */
	protected void onInstantiate() {
		if((this.something = this.getField(Id)) != null) {
			this.whatAmI = 2;
			this.setReturnType(((JavaField)this.something).getType());
		}
		else if((this.something = this.getFile().getImport(Id)) != null) {
			this.whatAmI = 1;
			this.setReturnType((JavaClass)this.something);
		}
		else
			this.setReturnType(null);
	}

	/**
	 * No visiting here, this should never be called.
	 */
	public void processExpression(JavaExpression expression) {
		JavaStatic.runtime.error("If you managed to reach this error, you've done something I didn't think was possible");
		JavaStatic.runtime.exit();
	}
}
