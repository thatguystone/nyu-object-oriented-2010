package SpecialCases;

/**
 * Provides a basic interface for dealing with special cases in the API.
 */
public abstract class SpecialCase {
	/**
	 * Gets a list of any extra fields that need to be added to the class to make it work in C++.
	 */
	public String[] getExtraFields() {
		return new String[0];
	}
	
	public String[] getExtraConstructors() {
		return new String[0];
	}
	
	public boolean shouldPrint() {
		return true;
	}
	
	public boolean shouldPrintHeader() {
		return true;
	}
	
	public boolean printTypedef() {
		return true;
	}
	
	public String getCppTemplate() {
		return "";
	}
}
