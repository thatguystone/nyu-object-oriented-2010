package translator;

/**
 * Provides a nice interface to Translator.  Rather than having everything in Translator, we come here
 * and do all of the processing.  This serves as a bridge from what the user wants to what needs to happen:
 * here is where the hard work begins.  We are given files to translate by Translator, and this takes care of
 * it.
 * 
 * Rather than creating a tree-like structure for our class heirachy, I'm going to
 * store everything in Hashtables so that we can do quick lookups based on names
 * alone, rather than having to traverse and find.  Everything in java (as with
 * any language), is represented by a string, so this is the quickest and easiest
 * way to store all our information.
 */
class JavaPackages {
	/**
	 * Singleton so that only 1 can exist.
	 */
	private static JavaPackages instance = null;

	/**
	 * Don't allow creation of the class anywhere but inside itself.
	 */
	private JavaPackages() {
		JavaStatic.pkgs = this;
	}
	
	/**
	 * Get the only instance of this class.
	 */
	public static JavaPackages getInstance() {
		if (instance == null)
			instance = new JavaPackages();
		
		return instance;
	}
}
