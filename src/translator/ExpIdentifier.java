package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * I can't believe I'm actually making this a class.... meh.
 * It holds a literal or a variable name, that's it.
 */
class ExpIdentifier extends JavaExpression {
	
	String value;
	
	ExpIdentifier(JavaScope scope, Node n) {
		this.node = n;
		this.value = (String)n.get(0);
		this.setScope(scope);
		this.setup();
	}

	private void setup(){
		if(this.getField(this.value) == null)
			this.getScope().getFile().getImport(value).activate();
	}

	public boolean isStatic() {
		if (this.getField(this.value) != null)
			return this.getField(value).isStatic();
		return true;
	}

	public boolean isClass() {
		if (this.getField(this.value) == null) {
			this.getScope().getFile().getImport((String)this.node.get(0)).activate();
			if (this.getScope().getFile().getImport((String)this.node.get(0)) != null)
				return true;
		}
		return false;
	}

	public JavaClass getType() {
		return this.getMyType();
	}

	public JavaClass getMyType() {
		this.getScope().getFile().getImport((String)this.node.get(0)).activate();
		return this.getCls().getFile().getImport((String)this.node.get(0));
	}

	public String printMe() {
		return value;
	}		
}
