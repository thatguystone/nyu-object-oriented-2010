package translator;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

/**
 * Create a visitor structure that only processes classes on demand. This makes our processing faster
 * as only files/classes that are actually needed are ever processed.
 */
abstract class ActivatableVisitor extends JavaScope {
	/**
	 * The Node that contains the information for this object.
	 */
	protected Node node = null;
	
	/**
	 * If the visitor has been activated / used.
	 */
	protected boolean activated = false; 
	
	/**
	 * Setup anything needed for this run. Right now, only saves the Node for use
	 * when activated.
	 *
	 * @param n The node with all the information.
	 */
	public void stashNode(Node n) {
		this.node = n;
	}
	
	/**
	 * Starts the processing (via Visitor.dispatch()) on this guy.
	 */
	public void activate() {
		if (this.activated)
			return;
		
		this.activated = true;
		
		this.process();
		
		//we don't need to hold on to the node any longer -- we've done all our processing, allow java to free it
		this.node = null;
	}
	
	/**
	 * For doing the dispatching on the item.  Sometimes they need to do more work before they are ready to dispatch,
	 * so give them that time here.
	 */
	abstract protected void process();
}
