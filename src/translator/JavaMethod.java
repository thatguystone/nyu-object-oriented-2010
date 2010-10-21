package translator;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import xtc.tree.Node;
import xtc.tree.GNode;

/**
 * JavaMethod is activatable so that it does not begin to process its internals before the class (and all its parents)
 * have been processed.  The problem with allowing it to activate on instantiation is that, while translating the method
 * bodies, they were running into classes that needed to be activated and were then being activated; normally, this would
 * not be a problem, but consider java.lang.Object: it depends on VMManager, and VMManager extends Object.  Thus, during
 * the translation, VMManager was being translated before any of java.lang.Object's methods were fully processed, so
 * VMManager didn't have any of its inherited methods.  We're now activating these methods after the parent methods have
 * been added to the VTables so that this no longer occurs.
 */
class JavaMethod extends ActivatableVisitor implements Nameable {	

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
	 * Sets things as static.
	 */
	private boolean isStatic = false;

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
		this.setup(n);
		this.dispatch(n);
		
		if (this.name.equals("main"))
			JavaClass.mainMethod = this;
	}
	
	public void process() {
		this.dispatch(this.node);
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
	 * Set the method as static.
	 */
	private void setStatic() {
		this.notVirtual();
		this.isStatic = true;
	}
	
	/**
	 * Test if this is a native method.
	 */
	public boolean isNative() {
		return this.isNative;
	}
	
	/**
	 * Test if this is a static method.
	 */
	public boolean isStatic() {
		return this.isStatic;
	}
	
	/**
	 * Find and return the C type value.
	 */
	public String getCReturnType() {
		String type;
		
		if (primitives.containsKey(this.returnType))
			type = primitives.get(this.returnType);
		else
			type = this.getCppReferenceScope(this.getFile().getImport(this.returnType));
		
		return type;
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
		String temp = "";
		if (!this.isStatic())
			temp = this.getCppReferenceScope(this.getParent()) + " __this";
		
		for (JavaField fld : this.parameters.values())
			temp = temp + ", " + fld.printpDec(false);
		
		//remove the extra ", " if we're static
		if (this.isStatic() && temp.length() > 0)
			temp = temp.substring(2);
		
		return temp;
	}

	/**
	 * Get only the types of the parameters for printing.
	 */
	public String getParameterTypes() {
		return this.getParameterTypes(this.getParent());
	}

	public String getParameterTypes(JavaClass cls) {
		String temp = "";
		if (!this.isStatic())
			temp = this.getCppReferenceScope(cls, false);
		
		for (JavaField fld : this.parameters.values())
			temp += ", " + fld.printFullType();
		
		//remove the extra ", " if we're static
		if (this.isStatic() && temp.length() > 0)
			temp = temp.substring(2);
		
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
		if (this.codeBlock != null) {
			return this.codeBlock.printBlock(block,
				this.getCReturnType()  + " __" +
				this.getParent().getName(false) + "::" + this.getName() + "(" + this.getParameters() + ")"
			);
		}
		
		return null;
	}
	
	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
	/**
	 * Translates the method body.
	 */
	public void visitBlock(GNode n) {
		if (this.activated)
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
		if (!primitives.containsKey(this.returnType))
			this.file.getImport(this.returnType).activate();
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
			if (modifier == "private")
				this.notVirtual();
			
			if (modifier == "static")
				this.setStatic();
			
			if (modifier == "native")
				this.setNative();
		}
	}
}
