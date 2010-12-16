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
	 * The string value that identifier has.
	 */
	private String nodeValue;
	
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
		this.nodeValue = (String)n.get(0);
		
		//rule 1: fields take precedence over classes
		this.fieldScope = this.getField(this.nodeValue);
		
		if (this.fieldScope != null) {
			this.cppValue = ((JavaField)this.fieldScope).getCppName(true); //be sure the field includes his accessor -- hence the "true"
			this.setType(((JavaField)this.fieldScope).getType());
			
			//fields can only be accessed cross-class...you cannot access any local fields!
			this.fieldScope = this.getType().getJavaClass();
		} else {
			//rule 2: classes are looked up after scope
			this.fieldScope = this.getJavaFile().getImport(this.nodeValue);
			
			if (this.fieldScope != null) {
				this.cppValue = ((JavaClass)this.fieldScope).getCppName(true, false);
				this.setType(((JavaClass)this.fieldScope).getType(), true); //class names should always be counted as static
			} else {
				//we didn't find a selectee -- since we're an identifier, set some value in Identifier so that our parent
				//knows that he should pull in our value and use that combined with his selectee value to attempt to find
				//what we are selecting.  This is for the: java.lang.System.out case.
				this.cppValue = "EXPRESSIONS.IDENTIFIER_ERROR";
				JavaStatic.runtime.error("Expressions.Identifier: Found an indentifier that wasn't a class or a field: " + this.nodeValue);
			}
		}
	}

	public String print() {
		if (this.isStaticAccess())
			return this.fieldScope.getJavaClass().getCppName(true, false);
		return this.cppValue;
	}
	
	public String getRawValue() {
		return this.nodeValue;
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
