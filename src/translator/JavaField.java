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
	 * Assignment statement associated with this field declaration.
	 * May not exist.
	 */
	private JavaExpression assignment = null;

	/**
	 * This constructor is for standard field declarations
	 */
	JavaField(GNode modifiers, JavaType type, int dimensions, JavaScope scope, GNode n) {
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

		this.dispatch(n);	
	}

	/**
	 * Gets the name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Does nothing.  Why would you use this?
	 */
	public String getName(boolean fullName) {
		return this.getName();
	}
	
	/**
	 * Gets the mangled name.
	 */
	public String getCppName() {
		if (this.mangledName == null)
			this.mangledName = this.name;
		
		return this.mangledName;
		
	}
	
	/**
	 * Does nothing.  Why would you use this?
	 */
	public String getCppName(boolean fullName) {
		return this.getCppName();
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
		
		fields.add(this.mangledName);
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

	public void print(CodeBlock b) {
		b.pln(this.type.getCppName() + " " + this.mangledName + ";");
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

/**
 * A special-case class for Formal Parameters -- allows us to reuse code from JavaField while taking into
 * account the different setup for FormalParameter.
 */
class FormalParameterField extends JavaField {
	FormalParameterField(GNode modifiers, JavaType type, int dimensions, JavaScope scope, GNode n) {
		super(modifiers, type, dimensions, scope, n);
	}

	protected void onInstantiate(GNode n) {
		this.name = (String)n.get(3);
		this.getScope().addField(this);
		this.dispatch(n);	
	}
}
