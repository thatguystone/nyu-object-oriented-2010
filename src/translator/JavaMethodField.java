package translator;

import xtc.tree.GNode;
import xtc.tree.Node;

/**
 * Not sure if this class is necessary. Will be necessary if a field needs to inquire about its own scope
 * or if the field needs to be stored in a list outside of it's method's field list
 */

class JavaClassField extends JavaField {

	/**
	 * Name of method that contains this field.
	 */
	//private JavaMethod method;

	JavaClassField(boolean isStatic, boolean isFinal, String type, int dimensions, /*JavaMethod method,*/ Node n) {
		super(isStatic, isFinal, type, dimensions, n);
		//this.method = method;
		//this.method.addField(this);
	}

	/*public JavaMethod getMethodName() {
		return this.Method;
	}*/
}
