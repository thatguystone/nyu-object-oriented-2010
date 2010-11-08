package translator;

/**
 * When something contains a type, this just provides a nice interface
 * for getting that type.
 */
public interface Typed {
	/**
	 * Get the type of this guy.
	 */
	public JavaType getType();
}
