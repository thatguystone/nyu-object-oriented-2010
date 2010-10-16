package translator;

import java.util.ArrayList;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

class JavaField extends ExpressionVisitor implements Nameable{

	/**
	 * The name of the field.
	 */
	private String name;
	
	/**
	 * Is this declaration static?
	 */
	private boolean isStatic = false;

	/**
	 * Is it an object?
	 */
	private boolean isObject = false;

	/**
	 * Is this declaration final.
	 * We don't care.
	 */
	private boolean isFinal = true;

	/**
	 * Number of array dimensions, 0 means not an array.
	 */
	private int dimensions = 0;

	/**
	 * Class this obj belongs to.
	 */
	private JavaClass cls;

	/**
	 * Type of the object represented as a string.
	 */
	private String type;

	/**
	 * This is used for testing if type is primitive
	 */
	private static ArrayList<String[]> primitives = new ArrayList<String[]>();

	/**
	 * Scope of the field
	 */
	private JavaScope scope;

	/**
	 * Assignment statement associated with this field declaration.
	 * May not exist.
	 */
	private JavaExpression assignment = null;

	JavaField(boolean isStatic, boolean isFinal, String type, int dimensions, JavaScope scope, Node n) {
		setPrimitives();
		this.name = (String)n.get(0);
		this.isStatic = isStatic;
		this.isFinal = isFinal;
		this.type = type;
		setType(type);
		this.dimensions = dimensions;
		this.scope = scope;
		this.node = n;
		this.scope.addField(this);
		this.dispatch(this.node);

		if(this.myExpressions.size() > 0) assignment = this.myExpressions.get(0);
	}

	private static void setPrimitives() {
		String[] p = {"byte", "short", "int", "long", "float", "double", "char", "boolean"};
		primitives.add(p);
	}

	//doesn't really do anything right now
	private void setType(String type) {
		if (!(primitives.contains(type))) {
			//this.scope.getFile().getImport(type).activate();
			//removed because of null ptr exception
			//this.cls = this.scope.getFile().getImport(type);
			this.isObject = true;
		}
	}

	public JavaScope getScope() {
		return this.scope;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Does this declaration come with an assignment?
	 */
	public boolean hasExpression() {
		if (this.assignment == null) return false;
		return true;
	}

	/**
	 * Returns just the data type.
	 * int x and int[] y both return int.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Call this when you need to print the declaration
	 */
	public String printMe() {
		if (hasExpression()) {
			return getType() + " " + getName() + " = " + assignment.printMe() + ";"; 
		}
		return getType() + " " + getName() + ";";
	}

	/**
	 * Call this when you only want to print the declaration without assignment
	 */
	public String printDec() {
		return getType() + " " + getName() + ";";
	}
	
	/**
	 * Call this when you only want to print the assignment without the declaration.
	 * This will cause problems if there was no assignment, but that case shouldn't come up.
	 */	
	public String PrintAssignment() {
		return getName() + " = " + assignment.printMe() + ";";
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
}
