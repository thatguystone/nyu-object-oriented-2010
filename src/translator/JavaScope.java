package translator;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

/**
 * Handles anything that can have its own scope, from a File to a Block.
 */
abstract class JavaScope extends Visitor implements ItemVisibility {
	/**
	 * The name of the package.  We include this in all scopes so that we don't have to climb
	 * the tree when printing: we can just directly reference it here.
	 */
	private String pkg;
	
	/**
	 * The scope that this scope is in (ie. the parent scope).
	 */
	private JavaScope scope;
	
	/**
	 * Our visibility.
	 */
	private Visibility visibility;
	
	/**
	 * If we do not have a parent scope, then use this constructor.
	 */
	JavaScope() {
		//Don't do anything.  Why would we?
	}
	
	/**
	 * Do some frikking-sweet calling.
	 */
	JavaScope(JavaScope scope) {
		this(scope, null);
	}
	
	/**
	 * Store our parent scope so that we can climb our scope tree.
	 */
	JavaScope(JavaScope scope, GNode n) {
		this.scope = scope;
		
		//if we have a scope, then save our package name
		if (scope != null)
			this.pkg = scope.pkg;
		
		this.visibility = this.getDefaultVisibility();
		
		//do the construct call-back, alright, do the construct call-back, baby.
		this.onInstantiate();
	}
	
	/**
	 * A nice, little call-back for when something is constructed.  Just in case classes
	 * need to do something without intercepting the constructor.
	 */
	protected void onInstantiate() { }
	
	/**
	 * ==================================================================================================
	 * Visibility Methods
	 */
	
	/**
	 * See if this item is visible at the specified level.
	 */
	public boolean getVisibility(Visibility v) {
		return this.visibility == v;
	}
	
	/**
	 * Set the default visibility for this scope item. Defaults to PackageProtected if not overriden.
	 */
	protected Visibility getDefaultVisibility() {
		return Visibility.PACKAGEPROTECTED;
	}
	
	/**
	 * Determine the visibility of the class. We need this before we visit.
	 */
	protected void setupVisibility(GNode modifiers) {
		if (modifiers == null)
			return;
		
		for (int i = 0; i < modifiers.size(); i++) {
			String mod = ((GNode)modifiers.get(i)).get(0).toString();
			if (mod.equals("public")) {
				this.visibility = Visibility.PUBLIC;
			} else if (mod.equals("protected")) {
				this.visibility = Visibility.PROTECTED;
			} else if (mod.equals("private")) {
				this.visibility = Visibility.PRIVATE;
			}
		}
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
	 * Gets the file that this scope is contained in.
	 */
	public JavaFile getFile() {
		if (this instanceof JavaFile)
			return (JavaFile)this.scope;
		
		if (this.scope == null) {
			JavaStatic.runtime.error("Epic fail. Some scope object was not contained in a file.");
			JavaStatic.runtime.exit();
		}
		
		//ask the parent if he is a file
		return this.scope.getFile();
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
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
