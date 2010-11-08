package translator;

import java.util.ArrayList;

import translator.Expressions.JavaExpression;

/**
 * Wraps the method signature for a method.  Capable of being compared to other signatures
 * in order to find the most specific signature for a given task (for overloading).
 */
public class JavaMethodSignature {
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
	 * @param f The field to be added.
	 */
	public void add(JavaType type, JavaScope s) {
		this.sig.add(new TypeContainer(type, s));
	}
}
