package translator;

import xtc.tree.GNode;
import xtc.tree.Node;

/**
 * Not sure if this class is necessary. Will be necessarys if a field needs to inquire about its own scope
 * or if the field needs to be stored in a list outside of it's class's field list
 */

class JavaClassField extends JavaField {

	/**
	 * Name of class that contains this field.
	 */
	private JavaClass cls;

	JavaClassField(boolean isStatic, boolean isFinal, String type, int dimensions, JavaClass cls, Node n) {
		super(isStatic, isFinal, type, dimensions, n);
		this.cls = cls;
		this.cls.addField(this);
	}

	public JavaClass getClassName() {
		return this.cls;
	}
}
