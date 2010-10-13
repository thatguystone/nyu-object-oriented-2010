package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

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
	 * If the method is virtual.  Assume it is, unless otherwise proven.
	 */
	private boolean isVirtual = true;
	
	/**
	 * If we are a constructor method.
	 */
	private boolean isConstructor = false;
	
	/**
	 * Runs the dispatcher on the node.
	 */
	JavaMethod(GNode n, JavaClass parent) {
		this.parent = parent;
		this.setProperties(n);
		
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
	 * Checks if a local variable exists, if it does lastID and lastScope will get set
	 */
	public boolean hasID(String ID) {
		/**
		 * @TODO implement
		 */
		return false;
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

	public String getID() {
		return this.getMethodSignature();
	}

	/**
	 * Same as getParent but visible to JavaScope
	 */
	public JavaScope getScope() {
		return this.getParent();
	}
	
	/**
	 * Gets a local variable
	 */
	public JavaScope getScopeFromID(String ID) {
		/**
		 * @TODO implement, I'm holding back on this because I don't think
		 * it'll end up being implemented here
		 */
		return null;
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
	 * Record the modifiers for the method.
	 */
	public void visitModifiers(GNode n) {
		for (int i = 0; i < n.size(); i++) {
			String modifier = n.get(i).toString();
			
			//surely there is a better way to do this...
			if (modifier == "private" || modifier == "static")
				this.notVirtual();
		}
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
