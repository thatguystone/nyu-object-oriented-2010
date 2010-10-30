package translator;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

/**
 * Handles anything that can have its own scope, from a File to a Block.
 */
class JavaScope extends Visitor {
	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
	/**
	 * The default visitor method from Visitor.
	 */
	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}
