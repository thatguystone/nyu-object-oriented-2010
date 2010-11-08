package translator;

/**
 * A set way of determining the visibility of something in java.
 */
interface ItemVisibility {
	/**
	 * See if this item is visible at the specified level.
	 */
	public boolean isVisible(Visibility v);
	
	/**
	 * See if the method is available at the specified level or greater. For example, if you are
	 * testing if a method is private, and it is public it will return true.  Or, if you are testing
	 * if a method is package protected, and it is private (or protected), it will return false.
	 */
	public boolean isAtLeastVisible(Visibility v);
}
