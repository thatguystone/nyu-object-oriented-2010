package translator;

/**
 * The possible visibility types out there.
 *
 * This is in a different file, not in JavaVisibleScope, because Java was complaining that it couldn't find it in JavaVisibleScope. W/e.
 */
public enum Visibility {
	PRIVATE,
	PROTECTED,
	PACKAGE_PROTECTED,
	PUBLIC;
}
