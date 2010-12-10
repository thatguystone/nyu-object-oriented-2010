package SpecialCases;

/**
 * Handles the stupid stuff for java.lang.String.
 */
public class java_lang_String extends SpecialCase {
	/**
	 * Gets a list of any extra fields that need to be added to the class to make it work in C++.
	 */
	public String[] getExtraFields() {
		return new String[]{"std::string __data"};
	}
	
	public String[] getExtraConstructors() {
		return new String[]{"__String(char* s) : __data(s) {}"};
	}
}
