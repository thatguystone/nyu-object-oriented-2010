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
		return this.printBlock(block, header, true);
	}

	public CodeBlock printBlock(CodeBlock block, String header, boolean withTrailingSemicolon) {
		block = block.block(header);
		
			if (this.getScope() instanceof JavaMethod) {
				if (((JavaMethod)this.getScope()).isConstructor()) {
					System.out.println("------------CONSTRUCTOR TIME-----------" + this.getCls().getName());
					for (JavaField fld : this.getCls().fields.values()) {
						System.out.println(fld.getName() + " ------------------ Static : " + (fld.isStatic()?"yes":"no") + "    " + "Expression : " + (fld.hasExpression()?"yes":"no"));
						if (!fld.isStatic() && fld.hasExpression()) {
							block.pln("this->" + fld.PrintAssignment());
						}
					}
				}
			}
		
			for (JavaScope statement : this.statements) {
				if (!statement.hasBlock()) {
					if (this.getScope() instanceof JavaMethod) {
						if (((JavaMethod)this.getScope()).isConstructor()) {
							String temp = statement.printMe();
							while (temp.charAt(0) == '_') {
								temp = temp.substring(1);
							}
							block.pln(temp);
						}
						else
							block.pln(statement.printMe());
					}
					else
						block.pln(statement.printMe());
				}
				else 
					block = statement.printBlk(block);
			}
		block = block.close(withTrailingSemicolon);
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



