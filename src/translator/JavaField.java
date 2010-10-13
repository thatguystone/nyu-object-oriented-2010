package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * All fields are either class or method fields
 */
class JavaField extends JavaScope implements Nameable {

	/**
	 * The name of the field.
	 */
	private String name;
	
	/**
	 * Is this declaration static.
	 */
	private boolean isStatic = false;

	/**
	 * Is this declaration final.
	 */
	private boolean isFinal = true;

	/**
	 * Number of array dimensions, 0 means not an array.
	 */
	private int dimensions = 0;

	/**
	 * Type of the object represented as a string.
	 */
	private JavaType type;

	/**
	 * Scope of the field
	 */
	private JavaScope scope;

	/**
	 * Assignment statement associated with this field declaration.
	 * May not exist.
	 */
	//private JavaExpression assignment = null;

	JavaField(boolean isStatic, boolean isFinal, String type, int dimensions, JavaScope scope, Node n) {
		this.name = (String)n.get(0);
		this.isStatic = isStatic;
		this.isFinal = isFinal;
		this.type = this.typeList.get(type);
		this.dimensions = dimensions;
		this.scope = scope;
		this.node = n;

		((JavaClass)this.scope).addField(name, this);
	}

	/**
	 * This must be run AFTER all classes have been activated and populated
	 * with a list of all thier methods and fields. Fields (along with methods
	 * and expressions) may contain pointers to classes, methods, and fields.
	 */
	protected void process() {
		this.dispatch(this.node);
		this.node = null;
	}

	public JavaScope getScope() {
		return this.scope;
	}

	public String getName() {
		return this.name;
	}

	public String getID() {
		return this.getName();
	}

	/**
	 * Returns just the data type.
	 * int x and int[] y both return int.
	 */
	public JavaType getType() {
		return this.type;
	}

	/**
	 * Returns all information about a field's type.
	 * int[] x would return int_Array_1.
	 */	
	/**
	 * Type is now represented as a JavaType
	 */
	/*public String getFullType() {
		if (this.dimensions == 0) return this.type;
		return this.type + "_Array_" + this.dimensions;
	}*/
	

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */

	public void visitDimensions(GNode n) {
		if (this.dimensions == 0 && this.node.get(1) != null) {
			for (Object o : (Node)this.node.get(1)) this.dimensions++;
		}
	}

	public void visitArrayInitializer(GNode n) {
		//assignment = new JavaArrayInitializer();
	}

	public void visitnewArrayExpression(GNode n) {
		//assignment = new JavaArrayExpression();
	}

	public void visitnewClassExpression(GNode n) {
		//assignment = new JavaClassExpression();
	}

	public void visit(GNode n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}
