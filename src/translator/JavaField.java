package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * All fields are either class or method fields
 */
abstract class JavaField extends Visitor implements Nameable {

	/**
	 * The name of the field.
	 */
	protected String name;
	
	/**
	 * Is this declaration static.
	 */
	protected boolean isStatic = false;

	/**
	 * Is this declaration final.
	 */
	protected boolean isFinal = true;

	/**
	 * Number of array dimensions, 0 means not an array.
	 */
	protected int dimensions = 0;

	/**
	 * Type of the object represented as a string.
	 */
	protected String type;

	protected Node node;

	/**
	 * Assignment statement associated with this field declaration.
	 * May not exist.
	 */
	//protected JavaExpression assignment = null;

	JavaField(boolean isStatic, boolean isFinal, String type, int dimensions, Node n) {
		this.name = (String)n.get(0);
		this.isStatic = isStatic;
		this.isFinal = isFinal;
		this.type = type;
		this.dimensions = dimensions;
		this.node = n;
	}

	protected void process() {
		this.dispatch(this.node);
		this.node = null;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Returns just the data type.
	 * int x and int[] y both return int.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Returns all information about a field's type.
	 * int[] x would return int_Array_1.
	 */
	public String getFullType() {
		if (this.dimensions == 0) return this.type;
		return this.type + "_Array_" + this.dimensions;
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */

	public void visitDimensions(GNode n) {
		if (this.dimensions == 0 && this.node.get(1) != null) {
			for (Object o : (Node)this.node.get(1)) this.dimensions++;
		}
	}

	public void visitnewArrayExpression(GNode n) {}

	public void visitnewClassExpression(GNode n) {}

	public void visitPrimaryIdentifier(GNode n) {}

	public void visitLiteral(GNode n) {}

	public void visit(GNode n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}
