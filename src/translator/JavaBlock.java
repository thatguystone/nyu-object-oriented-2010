package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.GNode;

class JavaBlock extends JavaScope {

	ArrayList<JavaScope> statements = new ArrayList<JavaScope>();

	JavaBlock(JavaScope scope, Node n) {
		this.setScope(scope);
		this.dispatch(n);
	}

	public CodeBlock printBlock(CodeBlock block, String header) {
		block = block.block(header);
			for (JavaScope statement : this.statements) {
				if (!statement.hasBlock())
					block.pln(statement.printMe());
				else 
					block = statement.printBlk(block);
			}
		block = block.close();
		return block;
	}

	public void visitFieldDeclaration(GNode n) {
		JavaFieldDec temp = new JavaFieldDec(this.getScope(), this.getScope().getFile(), n);
		ArrayList<JavaField> fields = temp.getFields();
		for (JavaField fld : fields)
			this.statements.add(fld);
	}

	public void visitExpressionStatement(GNode n) {
		this.statements.add(new JavaExpressionStatement(this.getScope(), n));
	}

	public void visitReturnStatement(GNode n) {
		this.statements.add(new JavaReturnStatement(this.getScope(), n));
	}
	
	public void visitConditionalStatement(GNode n) {
		this.statements.add(new JavaConditionalStatement(this.getScope(), n));
	}

	public void visitForStatement(GNode n) {
		this.statements.add(new JavaForStatement(this.getScope(), n));
	}
	
	public void visitWhileStatement(GNode n) {
		this.statements.add(new JavaWhileStatement(this.getScope(), n));
	}

}



