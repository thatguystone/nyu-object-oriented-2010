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
	 * The visibility of the declaration.
	 */
	protected GNode modifiers;

	/**
	 * Number of array dimensions, 0 means not an array.
	 */
	protected int dimensions = 0;

	/**
	 * Type of the object represented as a string.
	 */
	protected JavaType type;

	/**
	 * Scope of this declaration
	 */
	protected JavaScope scope;
	
	/**
	 * F'ing shutup, Java! (For FormalParameters)
	 */
	protected FieldDec() {}
	
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
		String type = "";
		for (Object o : ((GNode)n.get(0)))
			type += o + ".";
			
		//Good times with strings
		this.type = JavaType.getType(this.scope, type.substring(0, type.length() - 1));
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
		JavaField field = new JavaField(this.modifiers, this.type, this.dimensions, this.scope, n);
		//this.fields.add(field);
	}
	
	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}

/**
 * For building up FormalParameters for JavaMethod.
 */
class FormalParameters extends FieldDec {
	/**
	 * Storage for our little node.
	 */
	private GNode node;

	/**
	 * A nice little constructor so that we can use this for JavaMethod::FormalParameter.
	 */
	public FormalParameters(JavaScope s, GNode n) {
		this.scope = s;
		this.node = n;
	}

	public JavaField getField() {
		this.visit((Node)this.node);
		
		return new FormalParameterField(this.modifiers, this.type, this.dimensions, this.scope, this.node);
	}
}
