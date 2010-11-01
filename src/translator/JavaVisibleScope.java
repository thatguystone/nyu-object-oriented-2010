package translator;

import xtc.tree.GNode;

/**
 * Handles anything that can haz a visibility modifier on it.  Since most things
 * do not have these modifiers, I chose not to give them to _every_ scope object.
 */
abstract class JavaVisibleScope extends JavaScope implements ItemVisibility {
	/**
	 * Our visibility.
	 */
	private Visibility visibility;
	
	/**
	 * If we are static.
	 */
	private boolean isStatic = false;

	/**
	 * We don't have a node.  Cool.
	 */
	JavaVisibleScope(JavaScope scope) {
		this(scope, null);
	}

	/**
	 * We don't have a scope.  Cool.
	 */
	JavaVisibleScope(GNode n) {
		this(null, n);
	}
	
	/**
	 * We have a scope. Cool.
	 */
	JavaVisibleScope(JavaScope scope, GNode n) {
		super(scope, n);
		this.visibility = this.getDefaultVisibility();
	}
	
	/**
	 * ==================================================================================================
	 * Utility Methods
	 */
	
	/**
	 * Tell people if we're static or not.
	 */
	public boolean isStatic() {
		return this.isStatic;
	}
	
	/**
	 * ==================================================================================================
	 * Visibility Methods
	 */
	
	public Visibility getVisibility() {
		return this.visibility;
	}
	
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
			} else if (mod.equals("static")) {
				this.isStatic = true;
			}
		}
	}
}
