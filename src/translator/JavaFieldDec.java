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
class JavaFieldDec extends Visitor {

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
	private String type;

	/**
	 * Scope of this declaration
	 */
	private JavaScope scope;

	/**
	 * Constructor for class declarations.
	 */
	JavaFieldDec(JavaScope scope, Node n) {
		this.scope = scope;
		this.dispatch(n);
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */

	public void visitModifier(GNode n) {
		if((String)n.get(0) == "static") isStatic = true;
		if((String)n.get(0) == "final") isFinal = true;
	}

	public void visitType(GNode n) {
		type = ((GNode)n.get(0)).get(0).toString();
		this.scope.getFile().getImport(type).activate();
		//type = this.typeList.get((String)((GNode)n.get(0)).get(0));
		//counting the number of childern in a Dimensions node
		/*if (n.get(2) != null) {
			for (Object o : (Node)n.get(2)) dimensions++;
		}*/
		visit(n);
	}

	public void visitDimensions(GNode n) {
		for (Object o : n) {
			dimensions++;
		}
	}

	public void visitDeclarator(GNode n) {
	//Declarators always come after Modifiers and Type in our java AST
		JavaField field = new JavaField(this.isStatic, this.isFinal, this.type, this.dimensions, this.scope, (Node)n);
			
	}

	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}
