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
public class Identifier extends JavaExpression {
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
	 * My parent is a selection expression! I have no kid to push my work on to, damn.
	 */
	public Identifier(JavaScope scope, GNode n, String info) {
		super(scope, n);
		this.value += "." + info;
		this.setup(n);
	}
	
	/**
	 * Setup return type and figure out what this idendifier is identifiying.
	 */
	protected void onInstantiate(GNode n) {
		this.value = (String)n.get(0);
	}

	private void setup(GNode n) {
		//a temporary storage so that we don't have to do numerous calls to
		//getField(), getType() when we find a Field / Class.
		/*
		JavaScope type;
		String temp;
		int point;
		if ((point = value.indexOf(".")) != -1) {
			temp = value.substring(0, point);
			if ((type = this.getField(temp)) != null) {
				while (point != -1) {
					this.setType(((JavaField)type).getType());
					if (value.indexOf(".", point + 1) != -1) {
						temp = value.substring(point, (point = value.indexOf(".", point + 1)));
						type = this.getType().getCls().getField(temp)
					}
				}
			}
		}
		else {
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
				JavaStatic.runtime.error("Identifier isn't a field or class.");
			}
		}
		*/
	}

	public String print() {
		return this.value;
	}
}
