package translator;

import translator.Expressions.*;

import java.util.ArrayList;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class JavaField extends JavaVisibleScope implements Nameable, Typed {

	/**
	 * The name of the field.
	 */
	private String name;

	/**
	 * Is it an object?
	 */
	private boolean isObject;

	/**
	 * Number of array dimensions, 0 means not an array.
	 */
	private int dimensions = 0;

	/**
	 * Class this obj belongs to.
	 */
	private JavaType Type;

	/**
	 * Type of the object represented as a string.
	 */
	private String type;

	/**
	 * Assignment statement associated with this field declaration.
	 * May not exist.
	 */
	private JavaExpression assignment = null;

	/**
	 * This constructor is for standard field declarations
	 */
	JavaField(boolean isStatic, GNode modifiers, String type, int dimensions, JavaScope scope, Node n) {
		super(scope, (GNode)n);
		setupVisibility(modifiers);
		this.type = type;
		this.dimensions = dimensions;

	}

	protected void onInstantiate(GNode n) {
		this.name = (String)n.get(0);
		this.getScope().addField(this);

		this.dispatch(n);	
	}

	/**
	 * Gets the name.
	 */
	public String getName() {
		return this.name;
	}

	public String getName(boolean fullName) {
		return this.getName();
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
	public JavaType getType() {
		return this.Type;
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */

	/**
	 * You are a poorly declared array. Go die. <<-- LOL @ Leon
	 */
	public void visitDimensions(GNode n) {
		if (this.dimensions == 0) {
			for (Object o : (Node)n) this.dimensions++;
		}
	}
}
