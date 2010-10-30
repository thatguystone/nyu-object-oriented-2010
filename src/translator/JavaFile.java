package translator;

import xtc.tree.Node;
import xtc.tree.GNode;

/**
 * A representation of a java file.
 */
class JavaFile extends ActivatableVisitor {
	/**
	 * Store the file name for the file and pass off the node for future use.
	 */
	JavaFile(String fileName, Node n) {
		this.stashNode(n);
	}
	
	/**
	 * Override processing from ActivatableVisitor.  When we are activated, this is
	 * what we are going to need to run for the translation to take place.
	 */
	protected void process() {
	
	}
}
