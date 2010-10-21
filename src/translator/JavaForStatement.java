package translator;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import xtc.tree.Node;
import xtc.tree.GNode;

class JavaForStatement extends JavaScope implements Nameable {

	JavaBasicForControl control;
	
	JavaBlock codeBlock;
	
	JavaForStatement(JavaScope scope, Node n) {
		System.out.println("################FOR STATEMENT###########");
		this.setScope(scope);
		//this.node = n;
		this.dispatch(n);
	}
	
	public boolean hasBlock() {
		return true;
	}
	
	public JavaFile getFile() {
		return this.getScope().getFile();
	}
	
	public CodeBlock printBlk(CodeBlock block) {
		block = this.codeBlock.printBlock(block,"for("+ control.printMe() +")");
		
		return block;
	}	
	
	
	public void visitBasicForControl(GNode n) {
		this.control = new JavaBasicForControl(this, n);
	}
	
	public void visitBlock(GNode n) {
		/**
		 * @TODO Implement!
		 */
		this.codeBlock = new JavaBlock(this, n);
	}
}
