package SpecialCases;

public class java_util_Array extends SpecialCase {
	public boolean shouldPrintHeader() {
		return false;
	}
	
	public boolean printTypedef() {
		return false;
	}
	
	public String getCppTemplate() {
		return "template <typename T>";
	}
}
