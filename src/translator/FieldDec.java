package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * A purely intermediary class designed to separate fields from their declarations
 * for their respective class/method. Get's its own class because it doesn't make
 * sense to have this be part of JavaClass or JavaMethod(if that's what we're calling
 * it) and this can't be part of JavaField.
 *
 * Justification for separating declarations: in java we can declare a type variable
 * and a type array in the same field declaration, with arrays being their own type
 * post-translation this is no longer possible
 */
public class FieldDec extends Visitor {

	/**
	 * Is this declaration static.
	 */
	private boolean isStatic = false;

	/**
	 * The visibility of the declaration.
	 */
	private GNode modifiers;

	/**
	 * Number of array dimensions, 0 means not an array.
	 */
	private int dimensions = 0;

	/**
	 * Type of the object represented as a string.
	 */
	private JavaType type;

	/**
	 * Scope of this declaration
	 */
	private JavaScope scope;

	/**
	 * Constructor. Not much else to say here.
	 */
	public FieldDec(JavaScope scope, GNode n) {
		this.scope = scope;
		this.dispatch(n);
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */

	/**
	 * Visit a modifier, only do something if it's one we care about(there aren't many).
	 */
	public void visitModifier(GNode n) {
		this.modifiers = n;
	}

	/**
	 * Get the type of the field represented as a string
	 */
	public void visitType(GNode n) {
		String name = ((GNode)n.get(0)).getName();
		
		if (name.equals("PrimitiveType")) {
			this.type = JavaType.getType(((GNode)n.get(0)).get(0).toString());
		} else if (name.equals("QualifiedIdentifier")) {
			String type = "";
			
			for (Object o : ((GNode)n.get(0)))
				type += o + ".";
			
			//Good times with strings
			this.type = JavaType.getType(this.scope, type.substring(0, type.length() - 1));
		}
	}

	/**
	 * You are an array. oh joy.
	 */
	public void visitDimensions(GNode n) {
		for (Object o : n) {
			dimensions++;
		}
	}

	/**
	 * Make an actual field.
	 */
	public void visitDeclarator(GNode n) {
	//Declarators always come after Modifiers and Type in our java AST
		JavaField field = new JavaField(this.isStatic, this.modifiers, this.type, this.dimensions, this.scope, (Node)n);
		//this.fields.add(field);
	}

	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}
