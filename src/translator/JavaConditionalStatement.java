package translator;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import xtc.tree.Node;
import xtc.tree.GNode;

class JavaConditionalStatement extends ExpressionVisitor implements Nameable {

	JavaExpression condition;
	
	JavaBlock codeBlock;
	
	JavaConditionalStatement(JavaScope scope, Node n) {
		this.setScope(scope);
		this.node = n;
		this.dispatch(n);
		this.condition = myExpressions.get(0);
	}
	
	public boolean hasBlock() {
		return true;
	}
	
	public CodeBlock printBlk(CodeBlock block) {
		block = this.codeBlock.printBlock(block,"if("+ condition.printMe() +")");
		
		return block;
	}	
	
	public void visitBlock(GNode n) {
		/**
		 * @TODO Implement!
		 */
		this.codeBlock = new JavaBlock(this, n);
	}
}

