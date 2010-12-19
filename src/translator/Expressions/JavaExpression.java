package translator.Expressions;

import translator.JavaScope;
import translator.JavaType;
import translator.JavaClass;
import translator.Typed;
import xtc.tree.GNode;

/**
 * Just to use as a type identifier
 */
abstract public class JavaExpression extends JavaScope implements Typed {
	/**
	 * The return type of this expression.
	 */
	protected JavaType returnType;
	
	/**
	 * If our type is being used in a static context.
	 */
	private boolean isTypeStatic;
	
	/**
	 * Determines what type of access is being done on us.  This is set by the parent so that we know how to print properly.
	 * This is necessary because our parent needs to inform us of what we need to print as based on what he is accessing;
	 * for example:
	 *
	 * 1. If the parent is accessing a static method, then we need to print out with our class type so it can be found.
	 * 2. If our parent is accessing a non-static method of a static property, the property needs to print out in non-static
	 *    form so that we can do our access correctly.
	 * 3. If the parent is accessing a static property but nothing else, then we should return his type, as any future
	 *    calls will be based on the type of the static property....I think?
	 */
	private boolean isStaticAccess;
	
	/**
	 * Determines if we are doing a static set of something.
	 */
	private boolean isStaticSet = false;
	
	/**
	 * Is this expression part of a method chain?
	 */
	protected boolean chaining;
	
	public JavaExpression(JavaScope scope, GNode n)	{
		super(scope, n);
	}

	/**
	 * Get the type that this expression returns once everything inside it is done.
	 */
	public JavaType getType() {
		return this.returnType;
	}

	/**
	 * Set the return type of this expression.
	 */
	public void setType(JavaType type) {
		this.setType(type, false);
	}
	
	/**
	 * Set the return type of this expression.
	 */
	public void setType(JavaType type, boolean isStatic) {
		this.returnType = type;
		this.isTypeStatic = isStatic;
	}

	public boolean isTypeStatic() {
		return this.isTypeStatic;
	}
	
	public boolean isStaticAccess() {
		return this.isStaticAccess;
	}
	
	public boolean isStaticAccess(boolean access) {
		return this.isStaticAccess = access;
	}
	
	public boolean isStaticSet() {
		return this.isStaticSet;
	}
	
	public boolean isStaticSet(boolean set) {
		return this.isStaticSet = set;
	}
	
	public boolean partOfChain() {
		if (this.chaining)
			return true;
		
		if (this.getScope() == null)
			return false;
		
		JavaScope p = this.getScope();
		if (p instanceof JavaExpression)
			return ((JavaExpression)p).partOfChain();
			
		return false;
	}
	
	/**
	 * The print method...really, it returns a string to be printed.
	 */
	public abstract String print();

	/**
	 * Identifiers need to be able to print more than one thing
	 */
	public String print(boolean forIdentifiers) {
		return this.print();
	}
}
