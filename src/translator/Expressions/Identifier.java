package translator.Expressions;

import java.util.LinkedHashMap;

import translator.JavaClass;
import translator.JavaFile;
import translator.JavaField;
import translator.JavaScope;
import translator.JavaType;
import translator.JavaStatic;
import translator.Nameable;
import xtc.tree.GNode;

public class Identifier extends JavaExpression {
	/**
	 * The value to be printed for this guy in C++.
	 */
	private String cppValue;
	
	/**
	 * The scope in which our field exists.
	 */
	protected JavaScope fieldScope;
	
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
		String name = (String)n.get(0);
		
		//rule 1: fields take precedence over classes
		//and we need to get our field from our parent scope, not our overriden getField() below
		this.fieldScope = this.getField(name);
		
		if (this.fieldScope != null) {
			this.cppValue = ((JavaField)this.fieldScope).getCppName(false);
			this.setType(((JavaField)this.fieldScope).getType());
			
			//fields can only be accessed cross-class...you cannot access any local fields!
			this.fieldScope = this.getType().getJavaClass();
		} else {
			//rule 2: classes are looked up after scope
			this.fieldScope = this.getJavaFile().getImport(name);
			
			if (this.fieldScope != null) {
				this.cppValue = ((JavaClass)this.fieldScope).getCppName(true, false);
				this.setType(((JavaClass)this.fieldScope).getType());
			} else {
				this.cppValue = "EXPRESSIONS.IDENTIFIER_ERROR";
				JavaStatic.runtime.error("Expressions.Identifier: Found an indentifier that wasn't a class or a field: " + name);
			}
		}
	}

	public String print() {
		return this.cppValue;
	}
	
	/**
	 * We're going to be tricky -- override getField so that we can use our field's scope as the
	 * scope from which to resolve further fields.
	 */
	public JavaField getField(String name) {
		//if we haven't set our field scope yet, just use our parent's so he can still access stuff
		if (this.fieldScope == null)
			return this.getScope().getField(name);
		
		return this.fieldScope.getField(name);
	}
}
