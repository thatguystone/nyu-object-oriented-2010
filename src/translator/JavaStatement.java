package translator;

import xtc.tree.GNode;

/**
 * For statements, someone will implement this eventually.
 */
abstract public class JavaStatement extends JavaScope {

	public JavaStatement (JavaScope scope, GNode n) {
		super(scope, n);
	}

}
