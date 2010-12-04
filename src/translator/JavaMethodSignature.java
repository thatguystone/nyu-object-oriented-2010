package translator;

import java.util.ArrayList;

import translator.Expressions.JavaExpression;

/**
 * Wraps the method signature for a method.  Capable of being compared to other signatures
 * in order to find the most specific signature for a given task (for overloading).
 */
public class JavaMethodSignature {
	/**
	 * A simple container for holding the paramter types and 
	 * the parameters themselves.
	 */
	private class TypeContainer {
		public JavaType type;
		public JavaScope item;
		
		public TypeContainer(JavaType type, JavaScope scope) {
			this.type = type;
			this.item = scope;
		}
	};

	/**
	 * The fields, in the order they were added (for obvious reasons).
	 */
	private ArrayList<TypeContainer> sig = new ArrayList<TypeContainer>();
	
	/**
	 * Add anything that can be done in a method call to the signature
	 *
	 * @param type The type of the parameter.
	 * @param s The parameter itself -> JavaScope is the parent of it, so this allows it to print.
	 */
	public void add(JavaType type, JavaScope s) {
		this.sig.add(new TypeContainer(type, s));
	}
	
	/**
	 * Goes through all of the arguments and returns a simple string representation of them for name mangling.
	 */
	public String getCppMangledArgumentList() {
		if (this.sig.size() == 0)
			return "";
	
		String ret = "_";
		for (TypeContainer c : this.sig) {
			ret += c.type.getName() + "_";
		}
		
		return ret.substring(0, ret.length() - 1).replace(".", "_");
	}
	
	/**
	 * Gets a C++ printable version of the arguments.
	 */
	public String getCppArguments() {
		return this.getCppArguments(true);
	}
	
	/**
	 * Gets a C++ printable version of the arguments.
	 *
	 * @param withVariableNames If the variable name should also be returned in the argument list.
	 */
	public String getCppArguments(boolean withVariableNames) {
		if (this.sig.size() == 0)
			return "";
	
		String ret = "";
		
		for (TypeContainer c : this.sig) {
			System.out.println(c.item.getClass().getName());
			//ret += c.type.getCppName() + (withVariableNames ? c.item.getCppName() : "") + ",";
		} 
		return "";
		
		//return ret.substring(0, ret.length() - 1);
	}
	
	/**
	 * Compares a signature to another to see if they are equal.
	 */
	public boolean equals(JavaMethodSignature sig) {
		//if their signatures aren't the same length, we've got problems
		if (this.sig.size() != sig.sig.size())
			return false;
		
		//signatures are the same length, let's run through the parameters
		for (int i = 0; i < this.sig.size(); i++) {
			if (this.sig.get(i).type != sig.sig.get(i).type)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the number of arguments contained in this signature.
	 */
	public int size() {
		return this.sig.size();
	}
	
	/**
	 * Determines if this signature can be used to identify the given signature.  That is, if this
	 * signature is: <br><br>
	 * 
	 *  1) The same length as the given<br>
	 *  2) The arguments for the given are at least as equal to this (ie. primitives can be expanded
	 *     to whatever given, classes are equal to or are parent classes of the others, etc).<br> 
	 *  3) The planets are aligned properly.<br><br>
	 *
	 * This can be a confusing call, so I'll explain with an example:<br><br>
	 * 
	 * Assume: this.sig = {Object, Object}; sig.sig = {Object, String}<br>
	 * For the first arguments, isChildOf will return true (Object is a child of Object, no matter which way compared)<br>
	 * For the second arguments, we will see if String (from sig.sig) is a child of Object (from this.sig)<br>
	 * It will return true, so we're good, and sig.sig can be used as (Object, Object)<br><br>
	 *
	 * On the opposing side, assume: this.sig = {Object, String}; sig.sig = {Object, Object}<br>
	 * It would then return false as Object IS NOT a child of String, so (Object, Object) CANNOT be used as (Object, String)
	 */
	public boolean canBeUsedAs(JavaMethodSignature sig) {
		if (this.size() != sig.size())
			return false;
		
		//go through all of our internal arguments, and compare them to the other's
		for (int i = 0; i < this.size(); i++) {
			if (!sig.sig.get(i).type.isChildOf(this.sig.get(i).type))
				return false;
		}
		
		return true;
	}
}
