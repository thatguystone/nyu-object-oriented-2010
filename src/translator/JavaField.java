package translator;

import translator.Expressions.*;
import translator.Printer.CodeBlock;
import translator.Printer.CodePrinter;

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
	private int dimensions;

	/**
	 * Class this obj belongs to.
	 */
	private JavaType type;
	
	/**
	 * Makes sure that a field only prints himself to a block once.
	 */
	private boolean isPrinted = false;
	
	/**
	 * If we have a static variable that points to a class, we need to have a static wrapper
	 * on it to mimic java's static accesses.
	 */
	private boolean needsStaticWrapper; 
	
	/**
	 * Assignment statement associated with this field declaration.
	 * May not exist.
	 */
	protected JavaExpression assignment;
	
	/**
	 * Holds the list of array types that have had their __class() specialized.
	 */
	private static HashSet<String> initializedList = new HashSet<String>();
	
	private static CodeBlock arraySpecialize = new CodeBlock();
	private static CodeBlock arraySpecializeBlock;
	
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
		
		//save the dimensions from our parent
		this.dimensions = dimensions;
		
		//see if we have any dimensions of our own
		this.dispatch((GNode)n.get(1));

		//if we have dimensions, then import Array
		if (this.dimensions != 0) {
			this.getJavaFile().getImport("java.util.Array");
			this.getJavaFile().getImport("java.lang.ArrayIndexOutOfBoundsException");
		}

		if (!this.name.equals("args")) {
			this.type = JavaType.getType(this.type.getName(), this.dimensions);
		}

		this.needsStaticWrapper = (this.type.getJavaClass() != null);
		
		this.specializeArrayType();
	}
	
	public static void prepare() {
		arraySpecializeBlock = arraySpecialize.block("namespace java").block("namespace util");
	}
	
	public static void wrapUp() {
		arraySpecializeBlock.closeAll();
		JavaStatic.cpp.p(CodePrinter.PrintOrder.IMPLEMENTATION, arraySpecialize);
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
	 * Gets the mangled name.
	 */
	public String getCppName(boolean fullName) {
		return this.getCppName(fullName, true);
	}
	
	/**
	 * Gets the C++ name for a field.  If the field is static, it gets the static accessor method, unless otherwise specified.
	 */
	public String getCppName(boolean fullName, boolean staticAccessor) {
		if (this.mangledName == null)
			this.mangledName = name; //local fields are never mangled, so their mangled name is their name
	
		String accessor = "";
		
		//if we're a field at the class level, then we need to include an accessor to get the field
		if (fullName && this.getScope() == this.getJavaClass()) {
			if (this.isStatic())
				accessor = this.getJavaClass().getCppName(true, false) + "::" + (staticAccessor && this.needsStaticWrapper ? this.getStaticAccessor() : this.mangledName);
			else
				accessor = "__this->" + this.mangledName;
		} else {
			if (this.isStatic())
				accessor = (staticAccessor && this.needsStaticWrapper ? this.getStaticAccessor() : this.mangledName);
			else
				accessor = this.mangledName;
		}
		
		return accessor;
	}
	
	/**
	 * Gets the static accessor method name for the field.
	 */
	private String getStaticAccessor() {
		return "__" + this.mangledName + "_get()";
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
		//if (dimensions != 0)
			//return JavaType.getType("java.util.Array");
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
		String ret = (withType ? this.type.getCppName(dimensions != 0) + " " : "") + this.getCppName();
		
		if (this.assignment != null)
			ret += " = " + this.assignment.print();
		
		return ret;
	}


	protected void specializeArrayType() {
		if (this.dimensions == 0)
			return;
		
		if (initializedList.contains(this.getType().getCppName()))
			return;
		
		initializedList.add(this.getType().getCppName());
		
		//our parent __class()
		String parent;
		
		//the string representation, in java, of the array type
		String rep = "["; //f-that, we're only doing this for single-dimensional arrays
			
		if (this.getType().isPrimitive()) {
			JavaClass repCls = JavaStatic.pkgs.getClass(this.getType().getRepresentingObjectName()); 
			rep += repCls.getName(false).substring(0, 1);
			parent = repCls.getCppName(true, false);
		} else {
			rep += "L" + this.getType().getJavaClass().getParent().getJavaClass().getName() + ";"; 
			parent = this.getType().getJavaClass().getParent().getCppName(true, false);
		}
		
		parent += "::__class()";
		
		arraySpecializeBlock
			.pln("template <>")
			.block("java::lang::Class java::util::__Array<" + this.getType().getCppName() + ">::__class()")
    			.block("static java::lang::Class k = new java::lang::__Class(", false)
    				.pln("java::lang::asString(\"" + rep + "\"),")
					.pln(parent + ",")
					.pln("java::lang::__Class::__class()")
				.close()
				.pln(");")
				.pln("return k;")
			.close()
		;
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
		this.getCppName();
		b.pln(
			(this.isStatic() ? "static " : "") + this.type.getCppName(dimensions != 0) + " " + this.mangledName + 
			(withAssignment && this.assignment != null ? " = " + this.assignment.print() : "") + ";"
		);
		
		//if we have a typedef that needs printing (this is only initialized for class variables)
		//also make sure we're not dealing with a primitive as our 
		if (this.typedefName != null && this.getType().getJavaClass() != null)
			b.pln("typedef " + this.getType().getJavaClass().getCppName(true, false) + " " + this.typedefName + ";");
		
		if (this.isStatic())
			b.pln("inline static " + this.type.getCppName(dimensions != 0) + " " + this.getStaticAccessor() + ";");
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

    public void printToSwitchStatement(CodeBlock b,CodeBlock c) {
        if (this.isPrinted)
                return;
        this.isPrinted = true;

        this.print(b, false);
		if (this.assignment!=null){
			c.pln(this.getCppName() + " = " + this.assignment.print()+";");
		}
    }

	public void constructorPrint(CodeBlock b) {
		if (this.assignment != null && !this.isStatic())
			b.pln("__this->" + this.getCppName(false) + " = " + this.assignment.print() + ";");
	}

	public void initializeInImplementation(CodeBlock b, JavaClass c) {
		String clsName = c.getJavaClass().getCppName(true, false);
		
		if (this.assignment != null && this.isStatic()) {
			b.pln(this.getType().getCppName() + " " + clsName + "::" + this.mangledName + " = " + this.assignment.print() + ";");
		} else if (this.isStatic()) {
			//if we have a static property without an assignment, and that property isn't an object,
			//then we need to initialize him to his default value
			b.pln(this.getType().getCppName() + " " + clsName + "::" + this.mangledName + (!this.needsStaticWrapper ? " = " + this.getType().getDefaultValue() : "") + ";");
		}
		
		if (this.needsStaticWrapper && this.isStatic()) {
			//get our NullPointerException for use
			JavaClass e = this.getJavaFile().getImport("NullPointerException");
			
			b
				.block("inline " + this.getType().getCppName() + " " + clsName + "::" + this.getStaticAccessor())
					.block("if (" + clsName + "::" + this.mangledName + ".raw())")
						.pln("return " + clsName + "::" + this.mangledName + ";")
					.close()
					.pln("throw " + e.getCppName(true, true) + "();")
				.close()
			;
		}
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */

	/**
	 * You are a poorly declared array. Go die. <<-- LOL @ Leon
	 */
	public void visitDimensions(GNode n) {
		this.dimensions = n.size();
	}
}
