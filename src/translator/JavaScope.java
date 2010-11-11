package translator;

import java.util.HashMap;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

//we need every expression type for visiting
import translator.Expressions.*;

/**
 * Handles anything that can have its own scope, from a File to a Block.
 */
public class JavaScope extends Visitor {
	/**
	 * The name of the package.  We include this in all scopes so that we don't have to climb
	 * the tree when printing: we can just directly reference it here.
	 */
	private String pkg;
	
	/**
	 * The scope that this scope is in (ie the parent scope).
	 */
	private JavaScope scope;
	
	/**
	 * List of all fields in this scope.
	 */
	private HashMap<String, JavaField> fields = new HashMap<String, JavaField>();

	/**
	 * Do some frikking-sweet calling.
	 */
	public JavaScope(JavaScope scope) {
		this(scope, null);
	}
	
	/**
	 * Store our parent scope so that we can climb our scope tree.
	 */
	public JavaScope(JavaScope scope, GNode n) {
		this.scope = scope;
		
		//if we have a scope, then save our package name
		if (scope != null)
			this.pkg = scope.pkg;
		
		//do the construct call-back, alright, do the construct call-back, baby.
		this.onInstantiate(n);
	}
	
	/**
	 * A nice, little call-back for when something is constructed.  Just in case classes
	 * need to do something without intercepting the constructor.
	 */
	protected void onInstantiate(GNode n) { }
	
	/**
	 * ==================================================================================================
	 * Scope Methods -- these operate on _this_ scope and the _parent_ scope.
	 */

	/**
	 * Gets the file that this scope is contained in.
	 */
	public JavaFile getJavaFile() {
		if (this instanceof JavaFile)
			return (JavaFile)this;
		
		if (this.scope == null) {
			JavaStatic.runtime.error("Epic fail. Some scope object was not contained in a file.");
			JavaStatic.runtime.exit();
		}
		
		//ask the parent if he is a file
		return this.scope.getJavaFile();
	}
	
	/**
	 * Stupid name conflict with java.lang.Object.getClass()
	 */
	public JavaClass getJavaClass() {
		if (this instanceof JavaClass)
			return (JavaClass)this;
		
		if (this.scope == null) {
			JavaStatic.runtime.error("Epic fail. Some scope object was not contained in a file.");
			JavaStatic.runtime.exit();
		}
		
		//ask the parent if he is a file
		return this.scope.getJavaClass();
	}

	public JavaField getField(String name) {
		System.out.println(name);
		System.out.println("Implement JavaScope.getField()");
	
		return null;
	}

	/**
	 * Perhaps I need my scope.
	 */
	public JavaScope getScope() {
		return this.scope;
	}
	
	/**
	 * ==================================================================================================
	 * Utility Methods
	 */
	
	/**
	 * Sets the package we are currently in.
	 */
	protected void setPackageName(String pkg) {
		this.pkg = pkg;
	}
	
	/**
	 * Gets the name of the package we are in.
	 */
	public String getPackageName() {
		return this.pkg;
	}

	/**
	 * Add a field to our list of fields.
	 */
	public void addField(JavaField fld) {
		this.fields.put(fld.getName(), fld);
	}
	
	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
	public JavaExpression visitCallExpression(GNode n) {
	System.out.println("hey there");
		return new CallExpression(this, n);
	}
	
	public JavaExpression visitPrimaryIdentifier(GNode n) {
		return new Identifier(this, n);
	}
	
	public JavaExpression visitStringLiteral(GNode n) {
		return new StringLiteral(this, n);
	}
	
	public void visitBlock(GNode n) {
		new JavaScope(this, n) {
			protected void onInstantiate(GNode n) {
				this.visit(n);
			}
		};
	}
	
	/**
	 * The default visitor method from Visitor.
	 */
	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}
