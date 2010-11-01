package translator;

import xtc.util.Runtime;
import xtc.tree.GNode;

/**
 * Contains some instances of objects that are used all over the place and that make
 * no sense to pass around.
 */
class JavaStatic {
	/**
	 * The instance of {@link Translator} that everyone should use.
	 */
	public static Translator translator;
	
	/**
	 * The instance of {@link JavaPackages} that everyone has to use.
	 */
	public static JavaPackages pkgs;
	
	/**
	 * A link to {@link xtc.util.Runtime} that is quicker to access here than to throw
	 * around like a monkey on crack.
	 */
	public static Runtime runtime;
	
	/**
	 * Sometimes, there is just no way to figure out what is going on without a stack trace.
	 * So here it is: Stack Trace, or GTFO.
	 */
	public static void strackTrace() {
		for (StackTraceElement e : Thread.currentThread().getStackTrace())
			System.out.println(e.toString());
		
		System.out.println();
	}
	
	/**
	 * Sometimes, we just need to dump a node, and that code is always a hassle to find.
	 *
	 * @param n The node to be dumped.
	 */
	public static void dumpNode(GNode n) {
		JavaStatic.runtime.console().format(n).pln().flush();
	}
}
