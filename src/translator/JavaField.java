package translator;

import translator.Expressions.*;
import translator.Printer.CodeBlock;

import java.util.ArrayList;
import java.util.HashSet;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * Represents a "standard" java field.
 */
public class JavaField extends JavaVisibleScope implements Nameable, Typed {
	/**
	 * The name of the field.
	 */
	protected String name;
	
	/**
	 * The mangled name of the field
	 * Format: ClassName__name
	 */
	private String mangledName;
	
	/**
	 * We need the type name in C++ (for the typedef) so that we can do static accesses on the class types.
	 */
	private String typedefName;

	/**
	 * Is it an object?
	 */
	private boolean isObject;

	/**
	 * Number of array dimensions, 0 means not an array.
	 */
	private int dimensions = 0;

	/**
	 * Class this obj belongs to.
	 */
	private JavaType type;
	
	/**
	 * Makes sure that a field only prints himself to a block once.
	 */
	private boolean isPrinted = false;
	
	/**
	 * Assignment statement associated with this field declaration.
	 * May not exist.
	 */
	protected JavaExpression assignment;
	
	/**
	 * For variables that should never be printed. (ie. for (int i = .... ) -- the "int i" shouldn't be printed
	 * to the list of statements (it will be picked up in JavaScope.visitBlock()), so we ignore it here.
	 */
	public static class ScopeField extends JavaField {
		ScopeField(GNode modifiers, JavaType type, int dimensions, JavaScope scope, GNode n) {
			super(modifiers, type, dimensions, scope, n);
		}
	
		/**
		 * Scope parameters never print to the method body.
		 */
		public void print(CodeBlock b) { }
		
		/**
		 * Scope parameters never print to the method body.
		 */
		public void print(CodeBlock b, boolean withAssignment) { }
	}
	
	/**
	 * A special-case class for Formal Parameters -- allows us to reuse code from JavaField while taking into
	 * account the different setup for FormalParameter.
	 */
	public static class FormalParameter extends ScopeField {
		FormalParameter(GNode modifiers, JavaType type, int dimensions, JavaScope scope, GNode n) {
			super(modifiers, type, dimensions, scope, n);
		}

		protected void onInstantiate(GNode n) {
			this.name = (String)n.get(3);
			this.getScope().addField(this);
			this.dispatch(n);	
		}
	}
	
	/**
	 * This constructor is for standard field declarations
	 */
	public JavaField(GNode modifiers, JavaType type, int dimensions, JavaScope scope, GNode n) {
		super(scope, n);
		setupVisibility(modifiers);
		this.type = type;
		this.dimensions = dimensions;
	}
	
	/**
	 * Adds the language reserved fields to our stuff so that we mangle names around our needed variables.
	 */
	public static void reserveNames(HashSet<String> fields) {
		fields.add("__chain");
	}

	protected void onInstantiate(GNode n) {
		this.name = (String)n.get(0);
		this.getScope().addField(this);
		this.dispatch((GNode)n.get(1));
		this.assignment = (JavaExpression)this.dispatch((GNode)n.get(2));
	}

	/**
	 * Gets the name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the mangled name.
	 *
	 * @param fullName If true, includes the acessor.
	 */
	public String getName(boolean fullName) {
		if (this.mangledName == null)
			this.mangledName = name; //local fields are never mangled, so their mangled name is their name
	
		String accessor = "";
		
		//if we're a field at the class level, then we need to include an accessor to get the field
		if (fullName && this.getScope() == this.getJavaClass()) {
			if (this.isStatic())
				accessor = this.getJavaClass().getCppName(true, false) + "::";
			else
				accessor = "__this->";
		}
		
		return accessor + this.mangledName;
	}
	
	/**
	 * Gets the mangled name.
	 */
	public String getCppName() {
		return this.getCppName(true);
	}
	
	/**
	 * Does nothing.  Why would you use this?
	 */
	public String getCppName(boolean fullName) {
		return this.getName(fullName);
	}
	
	/**
	 * Returns the name of the field as used in the typedef.
	 */
	public String getTypedefName() {
		return this.typedefName;
	}
	
	/**
	 * Mangles field names so that each field can be uniquely represented by
	 * its 'ClassName__FieldName'
	 * 
	 * It should be noted that we had to resort to this name mangling algorithm
	 * because Grimm has failed us all... twice
	 */
	public void mangleName(HashSet<String> fields) {
		this.mangledName = this.name;
		
		if (fields.contains(this.mangledName)) {
			String clsName = this.getJavaClass().getName().replace(".", "_");
		
			this.mangledName = clsName + "__" + this.name;
		
			//someone is screwing with our names to try to break it...outsmart them :)
			if (fields.contains(this.mangledName)) {
				//set up a counter, and just go until we find a name we can use
				int i = 0;
				while (fields.contains(this.mangledName))
					this.mangledName = clsName + "__" + (i++) + "_" + this.name;
			}
		}
		
		this.typedefName = "__" + this.mangledName + "_t";
		
		fields.add(this.mangledName);
		fields.add(this.typedefName);
	}
	
	/**
	 * Does this declaration come with an assignment?
	 */
	public boolean hasExpression() {
		if (this.assignment == null)
			return false;
		return true;
	}

	/**
	 * Returns just the data type.
	 * int x and int[] y both return int.
	 */
	public JavaType getType() {
		return this.type;
	}
	
	/**
	 * Gets the C++ field name with its type.  There is no semicolon included.
	 */
	public String getCppField() {
		return this.getCppField(true);
	}
	
	/**
	 * @param withType If the type should be returned with the C++ field name.
	 */
	public String getCppField(boolean withType) {
		String ret = (withType ? this.type.getCppName() + " " : "") + this.getCppName();
		
		if (this.assignment != null)
			ret += " = " + this.assignment.print();
		
		return ret;
	}
	
	/**
	 * Prints out a nice version of the field to C++.
	 *
	 * @param b The block to which the field should be printed.
	 */
	public void print(CodeBlock b) {
		this.print(b, false);
	}
	
	/**
	 * Prints out a nice version of the field to C++.
	 *
	 * @param b The block to which the field should be printed.
	 * @param withAssignment If the assignment of the field (if there is one) should be included.
	 */ 
	public void print(CodeBlock b, boolean withAssignment) {
		b.pln(
			(this.isStatic() ? "static " : "") + this.type.getCppName() + " " + this.getCppName(false) + 
			(withAssignment && this.assignment != null ? " = " + this.assignment.print() : "") + ";"
		);
		
		//if we have a typedef that needs printing (this is only initialized for class variables)
		//also make sure we're not dealing with a primitive as our 
		if (this.typedefName != null && this.getType().getJavaClass() != null)
			b.pln("typedef " + this.getType().getJavaClass().getCppName(true, false) + " " + this.typedefName + ";");
	}
	
	/**
	 * A protected version of print that only allows a field to print itself to a block once.
	 */
	public void printToBlock(CodeBlock b) {
		if (this.isPrinted)
			return;
		this.isPrinted = true;
		
		this.print(b, true);
	}
	
	public void initializeInImplementation(CodeBlock b, JavaClass c) {
		if (this.assignment == null || !this.isStatic())
			return;
		
		b.pln(this.getType().getCppName() + " " + c.getJavaClass().getCppName(true, false) + "::" + this.mangledName + " = " + this.assignment.print() + ";");
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */

	/**
	 * You are a poorly declared array. Go die. <<-- LOL @ Leon
	 */
	public void visitDimensions(GNode n) {
		if (this.dimensions == 0) {
			for (Object o : (Node)n)
				this.dimensions++;
		}
	}
}
