package translator;

import xtc.tree.GNode;

/**
 * Deals primarily with printing out blocks.  Doesn't have much functionality beyond that.
 */
public class JavaBlock extends JavaScope {
	/**
	 * Not that I don't love copy-paste, but seriously, java?
	 */
	public JavaBlock(JavaScope scope, GNode n) {
		super(scope, n);
		this.visit(n);
	}
}
