package SpecialCases;

/**
 * Provides a basic interface for dealing with special cases in the API.
 */
public abstract class SpecialCase {
	/**
	 * Gets a list of any extra fields that need to be added to the class to make it work in C++.
	 */
	public abstract String[] getExtraFields();
	
	public abstract String[] getExtraConstructors();
}
