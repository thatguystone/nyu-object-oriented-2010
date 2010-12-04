package translator.Expressions;

import java.util.LinkedHashMap;

import translator.JavaClass;
import translator.JavaField;
import translator.JavaScope;
import translator.JavaType;
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
	
	private String cppValue;
	
	/**
	 * Another stupid constructor :(
	 */
	public Identifier(JavaScope scope, GNode n) {
		super(scope, n);
		this.setup(n, true);
	}

	/**
	 * My parent is a selection expression! I have no kid to push my work on to, damn.
	 */
	public Identifier(JavaScope scope, GNode n, String info) {
		super(scope, n);
		String temp = value;
		this.value += "." + info;
		if (!temp.equals("System"))
			this.setup(n);
		else
			this.cppValue = value;
	}
	
	/**
	 * Setup return type and figure out what this idendifier is identifiying.
	 */
	protected void onInstantiate(GNode n) {
		this.value = (String)n.get(0);
	}

	private void setup(GNode n, boolean t) {
		//are we looking at a field?
		JavaScope type;
		System.out.println("IDENTIFIER: " + this.value);
		
		for (String s : this.getScope().fields.keySet()) {
			System.out.println("55555"+s);
		}
		if ((type = this.getField(this.value)) != null) {
			System.out.println("FIELD: " + this.value);
			this.type = IdentifierType.FIELD;
			this.setType(((JavaField)type).getType());
			this.cppValue = value;
		
		//perhaps a class?
		} else if ((type = this.getJavaFile().getImport(this.value)) != null) {
			System.out.println("CLASS: " + this.value);
			this.type = IdentifierType.CLASS;
			this.setType(((JavaClass)type).getType());
			this.cppValue = ((JavaClass)type).getCppName();
		
		//well, that was nothing
		} else {
			JavaStatic.runtime.error("Identifier isn't a field or class.");
		}
	}

	private void setup(GNode n) {
		//a temporary storage so that we don't have to do numerous calls to
		//getField(), getType() when we find a Field / Class.
		JavaScope type;
		String temp;
		int point;
		if ((point = value.indexOf(".")) != -1) {
			temp = value.substring(0, point);
			System.out.println("***** " + temp + " *****");
			if ((type = this.getField(temp)) != null) {
				this.cppValue = temp;
				this.fieldFinder(value.substring(point+1), ((JavaField)type).getType());
			}
			else {
				this.classFinder(value);
			}
		}
	}
	
	private void fieldFinder(String s, JavaType t) {
		int point = 0;
		String temp;
		JavaField fld;
		do {
			temp = s.substring(point, s.indexOf('.', point + 1));
			System.out.println("***** " + temp + " *****");
			point = s.indexOf('.', point + 1);
			t = t.getJavaClass().getField(temp).getType();
			this.cppValue += "::" + temp;
		}while (point != -1);
		this.setType(t);
	}

	private void classFinder(String s) {
		JavaClass cls;
		String temp;
		int point = 0;
		do {
			point = s.indexOf('.', point + 1);
			temp = s.substring(0, point);
			System.out.println("***** " + temp + " *****");
		}while ((cls = this.getJavaFile().getImport(temp)) == null);
		this.setType(cls.getType());
		this.cppValue = cls.getCppName();
		fieldFinder(value.substring(temp.length()), this.getType());
		
	}

	public String print() {
		return this.cppValue;
	}
}
