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
	
	public boolean isMoreSpecific(JavaMethodSignature orig, JavaMethodSignature compare) {
		System.out.println("Need to implement: JavaMethodSignature.isMoreSpecific()");
		return false;
	}
}
