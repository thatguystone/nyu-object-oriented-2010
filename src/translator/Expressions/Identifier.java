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
	 * The identifier (its string representation).
	 */
	private String value;
	
	/**
	 * The type of identifier we're looking at.
	 */
	private IdentifierType type;
	
	/**
	 * The value to be printed for this guy in C++.
	 */
	private String cppValue;
	
	/**
	 * If we are a selection expression, this is the scope that we need to check fields against.
	 */
	private JavaClass cls;
	
	/**
	 * Another stupid constructor :(
	 */
	public Identifier(JavaScope scope, GNode n) {
		super(scope, n);
		this.setup(n);
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
	
	/**
	 * Figures out what we are and sets up our return type.
	 */
	private void setup(GNode n) {
		//set our class to nothing -- we do this because fields aren't initialized until the constructor is done,
		//and we're still in the constructor here.
		this.cls = null;
		this.cppValue = "";
		
		//are we a selection expression?
		if (this.value.indexOf(".") > -1)
			this.doClass(this.value);
		else
			this.resolve(this.value);
	}
	
	/**
	 * When we have a selection expression, then we need to find our class.  Once we find our class, we pass off
	 * to doField to finish up.
	 *
	 * @param value The selection expression -- expects there to be a "." somewhere in there.
	 */
	private void doClass(String value) {
		//a bunch of temporary values we need to root through the selection expression
		String[] parts = value.split("\\.");
		
		//do a quick check -- fields take precedence over class names, so:
		//if we have a field named "f" and a class named "f", then the field "f"
		//if we used instead of "f"
		//TAKE SPECIAL NOTE: if we do something like this:
		//	Object java = new Object;
		//	-> java.lang.Integer.TYPE (or anything in java.*) becomes UNACCESSIBLE.
		//	
		//	This is part of java, so we will do that here, too.
		//
		//so only attempt to find a class if we don't have a field by the same name
		if (this.getField(parts[0]) == null) {
			String clsSelector = "";
			JavaFile file = this.getJavaFile();
			int i = 0;
			
			//run through all of our selector until we find the name of the class
			do {
				clsSelector += parts[i++] + ".";
			} while (i < parts.length && (this.cls = file.getImport(clsSelector.substring(0, clsSelector.length() - 1))) == null);
			
			//if we did not find a class, something probably went wrong
			if (this.cls == null) {
				JavaStatic.runtime.error("Expressions.Identifier: class was not found: " + value);
			} else {
				this.setType(cls.getType());
				this.cppValue = cls.getCppName();
				value = value.replace(clsSelector, "");
			}
		}
		
		this.resolve(value);
	}
	
	/**
	 * We're given some identifier, and we have to figure out what it is.  We can assume that we don't have a static
	 * class hanging on the front (that's handled in doClass()), so we're only looking at accesible fields.  From those
	 * fields, go through and resolve to the very last one what we're accesing.
	 */
	private void resolve(String value) {
		//do we have to resolve fields from fields of other objects?
		if (value.indexOf(".") > -1) {
			//setup our basic holders for iteration
			String[] parts = value.split("\\.");
			String clsSelector = "";
			int i;
			
			//holders that we use as we're looping through finding our stuff
			JavaScope fScope;
			JavaField f;
			
			//do we need to get our class from the first field?
			//case:
			//	f1 f = new f1();
			//	f.f.j.method();
			if (this.cls == null) {
				f = this.getField(parts[0]);
			
				if (f == null) {
					JavaStatic.runtime.error("Expressions.Identifier: Could not find first field in current scope: " + value);
					JavaStatic.runtime.exit();
				}
				
				fScope = f.getType().getJavaClass();
				i = 1; //we used parts[0] to get the class of the first one
			
			//the class was already specified, so we're good
			//case:
			//	class f1 { public static f }
			//	f1.f.j.method();
			} else {
			 	fScope = this.cls;
			 	i = 0; //we're looping through everything
			}
			
			//go through all the accesors
			do {
				f = fScope.getField(parts[i++]);
				
				if (f == null) {
					JavaStatic.runtime.error("Expressions.Identifier: Attempting to access a field from another field failed: " + value);
					JavaStatic.runtime.exit();
				}
				
				//find the field type
				fScope = f.getType().getJavaClass();
				
				//and add it to our output (with proper syntax for if it's static or not)
				if (this.cppValue.length() > 0)
					this.cppValue += (f.isStatic() ? "::" : "->");
				this.cppValue += f.getCppName();
			} while (i < parts.length);
			
			//our type is the last field we access
			this.setType(f.getType(), f.isStatic());
		
		//we don't have a "." in our name
		} else {
			//since we don't have a "." in our name, it's entirely possible that we're doing field accesses on our local scope
			JavaScope s;
			if (this.cls != null)
				s = this.cls;
			else
				s = this.getScope();
			
			//grab our field
			JavaField f = s.getField(value);

			//if we didn't find a field, then perhaps we're accessing a class
			//again, this falls in line with how java handles it: local field names take precedence over class names,
			//so we always have to resolve fields before we attempt to resolve classes
			if (f == null) {
				JavaClass lCls = s.getJavaFile().getImport(value);
				
				if (lCls == null) {
					JavaStatic.runtime.error("Expressions.Identifier: Attempting to access a field/class that was never declared: " + value);
					JavaStatic.runtime.exit();
				}
				
				//since we have a class, and THAT'S IT, the only thing we can be doing to it is a static access
				this.cppValue += lCls.getCppName(true, false);
				this.setType(lCls.getType(), true);
			} else {
				if (this.cppValue.length() > 0)
					this.cppValue += (f.isStatic() ? "::" : "->");
				this.cppValue += f.getCppName();
				
				this.setType(f.getType(), f.isStatic());
			}
		}
	}

	public String print() {
		return this.cppValue;
	}
}
