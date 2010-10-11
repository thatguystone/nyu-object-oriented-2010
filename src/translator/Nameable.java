package translator;

/**
 * Anything that has a uninque name (in its own context) can use this.
 */
interface Nameable {
	/**
	 * Gets the name of whatever is nameable.
	 */
	public String getName();
}
