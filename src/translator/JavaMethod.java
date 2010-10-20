package translator;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import xtc.tree.Node;
import xtc.tree.GNode;

class JavaMethod extends JavaScope implements Nameable {	

	/**
	 * The class we are a part of.  This is helpful for determing if we're a constructor, and etc.
	 */
	private JavaClass parent;
	
	/**
	 * The method signature.  Types, as Java sees them: Object(String, Object)
	 */
	private String signature = "";
	
	/**
	 * The name of the method.
	 */
	private String name;
	
	/**
	 * Return type for method.
	 */
	private String returnType;
	
	/**
	 * If the method is virtual.  Assume it is, unless otherwise proven.
	 */
	private boolean isVirtual = true;
	
	/**
	 * If we are a constructor method.
	 */
	private boolean isConstructor = false;
	
	/**
	 * If we are looking at a native method.
	 */
	private boolean isNative = false;

	/**
	 * This is used for testing if type is primitive.
	 * This really has to become a global.
	 */
	private static ArrayList<String> primitives;

	/**
	 * This method's code block.
	 */
	private JavaBlock codeBlock;

	/**
	 * List of all formal paraneters of this method.
	 * Parameter name -> Field Object
	 */
	protected LinkedHashMap<String, JavaField> parameters = new LinkedHashMap<String, JavaField>();
	
	/**
	 * Runs the dispatcher on the node.
	 */
	JavaMethod(GNode n, JavaFile file, JavaClass parent) {
		this.setPrimitives();
		this.parent = parent;
		this.setScope(parent);
		this.setFile(file);
		this.setProperties(n);
		this.setFile(parent.getFile());
		
		this.dispatch(n);
	}

	/*public void activateMe() {
		this.dispatch(n);
	}*/
	
	private static void setPrimitives() {
		if (!(primitives instanceof ArrayList)) {
			primitives = new ArrayList<String>();
			String[] p = {"byte", "short", "int", "long", "float", "double", "char", "boolean"};
			for(int i = 0; i < 8; i++)
				primitives.add(p[i]);
		}
	}

	/**
	 * Dumps out the format of the method GNode.
	 */
	private void dumpGNode(GNode n) {
		JavaStatic.runtime.console().format(n).pln().flush();
	}
	
	/**
	 * Sets the proper name of the function and if it's a constructor.
	 */
	private void setProperties(GNode n) {
		this.name = n.get(3).toString();
		
		this.isConstructor = (this.name == this.parent.getName());
	}

	/**
	 * Gets the name of the method.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Grabs our parent class.
	 */
	public JavaClass getParent() {
		return this.parent;
	}
	
	public JavaClass getCls() {
		return this.parent;
	}

	public JavaClass getReturnType() {
		return this.getFile().getImport(this.returnType);
	}

	/**
	 * Gets the method with its signature intact.
	 */
	public String getMethodSignature() {
		return this.getName() + "(" + this.signature + ")";
	}
	
	/**
	 * Gets the function parameters for C++;
	 *
	 * @param cls The name of the class this function is being put into.
	 */
	private String getCMethodParameters(String cls) {
		return cls + (this.signature.length() > 0 ? "," : "") + this.signature;
	}
	
	/**
	 * Gets the function type for C++.
	 *
	 * @param cls The name of the class to use for __this.
	 */
	/*public String getCMethodCast(String cls) {
	    return "(" + this.getCReturnType() + "(*)(" + this.getCMethodParameters(cls) + "))";
	}*/

	public String getCMethodCast(JavaClass cls) {
		return "(" + this.getCReturnType() + "(*)(" + this.getParameterTypes(cls) + "))";
	}
	
	/**
	 * Gets the function type for C++.
	 *
	 * @param cls The name of the class to use for __this.
	 */
	/*public String getCMethodType(String cls) {
	    return "(*" + this.getName() + ")(" + this.getCMethodParameters(cls) + ")";
	}*/

	public String getCMethodType(JavaClass cls) {
		return "(*" + this.getName() + ")(" + getParameterTypes(cls) + ")";
	}
	
	/**
	 * Gets the function signature for C++.
	 *
	 * @param cls The name of the class to use for __this.
	 */
	public String getCMethodSignature(String cls) {
	    return this.getName() + "(" + this.getCMethodParameters(cls) + ")";
	}

	/**
	 * Is this a virtual method?
	 */
	public boolean isVirtual() {
		return this.isVirtual;
	}
	
	/**
	 * Is this a constructor method?
	 */
	public boolean isConstructor() {
		return this.isConstructor;
	}

	/**
	 * Set the method as not being virtual.
	 */
	private void notVirtual() {
		this.isVirtual = false;
	}
	
	/**
	 * Set the method as being native.
	 */
	private void setNative() {
		this.isNative = true;
	}
	
	/**
	 * Test if this is a native method.
	 */
	public boolean isNative() {
		return this.isNative;
	}
	
	/**
	 * Find and return the C type value.
	 */
	public String getCReturnType() {
		//NOTE: we have to remove Class because we don't yet have java.lang.Class
		if (!(primitives.contains(this.returnType)) && this.returnType.compareTo("void") != 0 && this.returnType.compareTo("Class") != 0) {	
			this.getFile().getImport(returnType).activate();
			//this.getParent() = this.getFile().getImport(returnType);
			return this.getCppScopeTypeless(this.getScope(), this.getFile().getImport(returnType)) + this.returnType;
			//return this.returnType;
		}
		return this.returnType;
	}
	
	/**
	 * Print out the translation.
	 */
	public void printImplementation() {
		//CodeBlock block = this.hBlock("private void something(String something)");
	}

	/**
	 * Get the method's parameters for printing.
	 */
	public String getParameters() {
		String temp = this.getParent().getName(false) + " __this";
		for (JavaField fld : this.parameters.values())
			temp = temp + ", " + fld.printDec();
		return temp;
	}

	/**
	 * Get only the types of the parameters for printing.
	 */
	public String getParameterTypes() {
		String temp = this.getParent().getName(false);
		for (JavaField fld : this.parameters.values())
			temp = temp + ", " + fld.printFullType();
		return temp;
	}

	public String getParameterTypes(JavaClass cls) {
		String temp = cls.getName(false);
		for (JavaField fld : this.parameters.values())
			temp = temp + ", " + fld.printFullType(cls);
		return temp;
	}

	/**
	 * Get the method header to be placed in the class's struct
	 */
	public String getMethodHeader() {
		return this.getCReturnType() + " " + this.getName() + "(" + this.getParameterTypes() + ");";
	}

	/**
	 * Needs Implementation!
	 */
	public String getVTHeader() {
		return null;
	}

	/**
	 * Print out the translation. printImplementation() is not in use.
	 */
	public CodeBlock getMethodBlock(CodeBlock block) {
		//block = block.block(this.getCReturnType()  + " " + this.getParent().getName(false) + "::" + this.getName() + "(" + this.getParameters() + ")")
			
			//.close();

		block = this.codeBlock.printBlock(block, this.getCReturnType()  + " " + this.getParent().getName(false) + "::" + this.getName() + "(" + this.getParameters() + ")");

		return block;
	}
	
	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
	/**
	 * Translates the method body.
	 */
	public void visitBlock(GNode n) {
		/**
		 * @TODO Implement!
		 */
		this.codeBlock = new JavaBlock(this, n);
	}
	
	/**
	 * Grabs the formal parameters and adds them to our signature.
	 */
	public void visitFormalParameters(GNode n) {
		//only run is size > 0 -- we use substring, so we need this condition (substring(1) on an empty string = exception)
		if (n.size() > 0) {
			for (int i = 0; i < n.size(); i++) {
				//you'd have to be a lisp programmer for all these parenthesis to make sense...
				this.signature += "," + (String)(((GNode)(((GNode)((GNode)n.get(i)).get(1)).get(0))).get(0));
			}
			
			this.signature = this.signature.substring(1);
		}
		visit(n);
	}

	/**
	 * Visit our formal parameters and add them to our list of fields.
	 * We're using this to take advantage of the scope searching already implemented
	 * in fields.
	 * JavaFieldDec isn't needed here because we can't pass multiple fields with the same type declaration.
	 */
	public void visitFormalParameter(GNode n) {
		JavaField field = new JavaField((String)((GNode)((GNode)n.get(1)).get(0)).get(0), this, this.getFile(), n);
		this.parameters.put(field.getName(), field);
	}
	
	/**
	 * Grab the method return type.
	 */
	public void visitType(GNode n) {
		this.returnType = ((GNode)n.get(0)).get(0).toString();
	}
	
	/**
	 * Void has its own SPECIAL (as in "herp derp") type.
	 */
	public void visitVoidType(GNode n) {
		this.returnType = "void";
	}
	
	/**
	 * Record the modifiers for the method.
	 */
	public void visitModifiers(GNode n) {
		for (int i = 0; i < n.size(); i++) {
			String modifier = ((GNode)n.get(i)).get(0).toString();
			
			//surely there is a better way to do this...
			if (modifier == "private" || modifier == "static")
				this.notVirtual();
			
			if (modifier == "native")
				this.setNative();
		}
	}
}
