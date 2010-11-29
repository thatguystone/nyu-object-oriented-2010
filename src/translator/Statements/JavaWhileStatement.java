package translator.Statements;
import translator.JavaScope;
import translator.Expressions.ConditionalExpression;
import translator.Printer.CodeBlock;
import java.util.ArrayList;
import xtc.tree.Node;
import xtc.tree.GNode;
class JavaWhileStatement extends JavaStatement{
	JavaWhileStatement(JavaScope scope, GNode n){
		super(scope, n);
	}
	public CodeBlock printMe(CodeBlock block){
		return block.block("for ("+ ((ConditionalExpression)header).printMe() + ")",true).attach((CodeBlock)b.get(0)).close();
	}
}
