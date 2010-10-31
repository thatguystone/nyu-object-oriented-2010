package translator;

import xtc.tree.GNode;
import xtc.tree.Node;

/**
 * A representation of a java method.
 */
class JavaMethod extends ActivatableVisitor {
	/**
	 * SAEKJFA;WIE JF K;LSDFJ ASILD JFASD;IFJ!!!!!!! WHY DOES JAVA NOT INHERIT CONSTRUCTORS?!?!?!?!?!?!?!?!?!??!
	 * This feels so dirty and wrong.
	 */
	JavaMethod(GNode n) {
		super(n);
	}
	JavaMethod(JavaScope scope, GNode n) {
		super(scope, n);
	}

	/**
	 * Override processing from ActivatableVisitor.  When we are activated, this is
	 * what we are going to need to run for the translation to take place.
	 */
	protected void process() {
	
	}
}
