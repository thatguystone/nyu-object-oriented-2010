package translator;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import xtc.tree.Node;
import xtc.tree.GNode;

class JavaConditionalStatement extends ExpressionVisitor implements Nameable {

	JavaExpression condition;
	
	JavaBlock codeBlock1;
	JavaBlock codeBlock2;
	
	JavaConditionalStatement(JavaScope scope, Node n) {
		this.setScope(scope);
		this.node = n;
		this.dispatch(n);
		this.condition = myExpressions.get(0);
	}
	
	public boolean hasBlock() {
		return true;
	}
	
	public JavaFile getFile() {
		return this.getScope().getFile();
	}
	
	public CodeBlock printBlk(CodeBlock block) {
		
		block = this.codeBlock1.printBlock(block,"if("+ condition.printMe() +")", false);
		if(codeBlock2 != null)
			block = this.codeBlock2.printBlock(block, "else");
		
		return block;
	}	
	
	public void visitBlock(GNode n) {
		/**
		 * @TODO Implement!
		 */
		if(codeBlock1 == null)
			this.codeBlock1 = new JavaBlock(this, n);
		else
			this.codeBlock2 = new JavaBlock(this, n);
	}
}

