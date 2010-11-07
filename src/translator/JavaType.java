package translator;

import xtc.tree.Node;
import xtc.tree.GNode;

/**
 * The current setup for expressions requires this class.
 */
abstract public class JavaType extends ActivatableVisitor {

	JavaType() {
		this(null);
	}

	JavaType(Node n) {
		super((GNode)n);
	 }

	JavaType(JavaScope scope, Node n) {
		super(scope, (GNode)n);
	}

}

