package translator;

import xtc.tree.Visitor;
import xtc.tree.Node;

import java.util.Hashtable;

/**
 * This class is the superclass of any class that is built from a GNode.
 * Its purpose is to provide a generic way of looking at everything and to
 * assist in finding other objects of this class. For example this class will
 * allow method calls to find the method declaration, local class object declarations
 * to find their class, identifiers to find where the identifiers was declared, etc.
 *
 * Currently JavaFile extends this class through ActivatableVisitor. This is a problem and
 * needs to be changed. I might make this a subclass of ActivatableVisitor.
 */
abstract class JavaScope extends Visitor {

	/**
	 * List of all known types including primitives
	 * This structure is used by a field to find out what class(or primitive)
	 * it belongs to.
	 * Type name -> Class object/Primitive object(these are dummy objects)
	 */
	protected static Hashtable<String, JavaType> typeList = new Hashtable<String, JavaType>();

	/**
	 * The last JavaScope object found from an ID search
	 */
	protected JavaScope lastScope;

	/**
	 * The ID from the last successful ID search
	 */
	protected String lastID;

	/**
	 * Signature of a field or method. Not every scope will have a signature, for those that don't
	 * it is necessary to know that they don't have names.
	 */
	//protected String signature = null;

	/**
	 * Gets the scope containing this object, should be abstract, see class desc for why it isn't
	 */
	public JavaScope getScope() {
		return null;
	}

	/**
	 * Adds a field to a JavaScope, will likely implement a class called JavaBlock or something
	 * in the future, this will be moved there
	 */
	public void addField(String name, JavaField field) {
		//will throw an exception in the future
		//anything that can add a field will have its own implementation
	}

	/**
	 * The Node that contains the information for this object.
	 * Every class represented by a node should be a subclass of JavaScope.
	 */
	protected Node node = null;

	/**
	 * Fields and methods will have their own implementation, everything else
	 * returns null because they don't have identifications
	 *
	 * I actually don't know if this is needed. Might(probably) be removed
	 * in the future
	 */
	public String getID() {
		return null;
	}

	/**
	 * Checks to see if a scope contains a method or field declaration
	 */
	public boolean hasID(String ID) {
		return false;
	}

	/**
	 * Gets a Method or field declaration in a scope
	 */
	public JavaScope getScopeFromID(String ID) {
		return null;
	}

	/**
	 * Finds a declaration as long as it's an immediate child of one of this node's ancestors.
	 * Basically, if you have an identifier this will find its declaration(fields and methods).
	 */
	protected JavaScope findDeclaration(String ID) {
		if (this.getScope().hasID(ID)) {
			return this.getScope().getScopeFromID(ID);
		}
		return this.getScope().findDeclaration(ID);
	}
}

