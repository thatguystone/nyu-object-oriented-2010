package translator;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

abstract class JavaScope extends Visitor {
	/**
	 * Prints the information contained in this class to the output files.
	 */
	abstract public void print();

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
