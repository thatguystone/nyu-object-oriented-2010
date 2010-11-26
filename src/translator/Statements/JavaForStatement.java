package translator.Statements;
import translator.JavaScope;
import translator.Printer.CodeBlock;
import java.util.ArrayList;
import xtc.tree.Node;
import xtc.tree.GNode;
class JavaForStatement extends JavaStatement{
	JavaForStatement(JavaScope scope, GNode n){
		super(scope, n);
	}
	public JavaScope visitBasicForControl(GNode n){
		return new JavaBasicForControl(this,n);
	}
	public CodeBlock printMe(CodeBlock block){
		return block.block("for ("+ ((JavaBasicForControl)header).printMe() + ")",true).attach((CodeBlock)b.get(0)).close();
	}
}
