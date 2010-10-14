package translator;

import java.util.Hashtable;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

abstract class JavaScope extends Visitor {
	protected Hashtable<String, JavaVariable> variables = new Hashtable<String, JavaVariable>();

	/**
	 * Each scope is going to need a pointer to its file so that it can activate classes when
	 * they are run across.
	 */
	protected JavaFile file;

	/**
	 * Prints the information contained in this class to the output files.
	 */
	abstract public void print();
	
	/**
	 * Sets the java file that contains whatever this is.
	 */
	public void setFile(JavaFile file) {
		this.file = file;
	}
	
	/**
	 * Gets the file that is the parent of this class.
	 */
	public JavaFile getFile() {
		return this.file;
	}

	
	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	 
	public void visitFieldDeclaration(GNode n) {
		JavaVariable jVar = new JavaVariable(this, n);
		//this.variables.put(jVar.getName(), jVar);
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
