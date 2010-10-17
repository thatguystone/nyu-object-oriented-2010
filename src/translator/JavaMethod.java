package translator;

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
	 * Runs the dispatcher on the node.
	 */
	JavaMethod(GNode n, JavaFile file, JavaClass parent) {
		this.parent = parent;
		this.setScope(parent);
		this.setFile(file);
		this.setProperties(n);
		this.setFile(parent.getFile());
		
		this.dispatch(n);
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
	public String getCMethodCast(String cls) {
	    return "(" + this.getCReturnType() + "(*)(" + this.getCMethodParameters(cls) + "))";
	}
	
	/**
	 * Gets the function type for C++.
	 *
	 * @param cls The name of the class to use for __this.
	 */
	public String getCMethodType(String cls) {
	    return "(*" + this.getName() + ")(" + this.getCMethodParameters(cls) + ")";
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
		return this.returnType;
	}
	
	/**
	 * Print out the translation.
	 */
	public void printImplementation() {
		//CodeBlock block = this.hBlock("private void something(String something)");
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
