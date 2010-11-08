package translator;

/**
 * A standard way of accessing name information across various classes.
 */
public interface Nameable {
	/**
	 * Gets the fully qualified java name.
	 */
	public String getName();
	
	/**
	 * Gets the java name.
	 *
	 * @param fullName True for the fully-qualified java name; false for just the last part of the name.
	 */
	public String getName(boolean fullName);
}
