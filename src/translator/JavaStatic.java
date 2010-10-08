package translator;

import xtc.util.Runtime;

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
}
