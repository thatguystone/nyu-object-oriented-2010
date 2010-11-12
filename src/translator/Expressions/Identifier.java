package translator.Expressions;

import translator.JavaClass;
import translator.JavaField;
import translator.JavaScope;
import translator.JavaStatic;
import translator.Nameable;
import xtc.tree.GNode;

/**
 * It holds a name, that's it.
 */
public class Identifier extends JavaExpression implements Nameable {
	/**
	 * The identifier (its string representation).
	 */
	private String value;
	
	/**
	 * The type of identifier we're looking at.
	 */
	private IdentifierType type;
	
	/**
	 * Another stupid constructor :(
	 */
	public Identifier(JavaScope scope, GNode n) {
		super(scope, n);
	}
	
	/**
	 * Setup return type and figure out what this idendifier is identifiying.
	 */
	protected void onInstantiate(GNode n) {
		this.value = (String)n.get(0);
		
		//a temporary storage so that we don't have to do numerous calls to
		//getField(), getType() when we find a Field / Class.
		JavaScope type;
		
		//are we looking at a field?
		if ((type = this.getField(this.value)) != null) {
			this.type = IdentifierType.FIELD;
			this.setType(((JavaField)type).getType());
		
		//perhaps a class?
		} else if ((type = this.getJavaFile().getImport(this.value)) != null) {
			this.type = IdentifierType.CLASS;
			this.setType(((JavaClass)type).getType());
		
		//well, that was nothing
		} else {
			this.type = IdentifierType.PACKAGE;
			this.setType(null);
		}
	}
	
	/**
	 * ==================================================================================================
	 * Nameable Methods
	 */
	
	/**
	 * Some pretty name!
	 */
	public String getName() {
		return value;
	}
	
	/**
	 * It's an identifier -- it can't have a special name.
	 */
	public String getName(boolean na) {
		return value;
	}




	public String printMe() {}
}
