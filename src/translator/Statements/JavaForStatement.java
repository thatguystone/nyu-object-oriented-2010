package translator.Statements;
import translator.JavaScope;
import translator.Printer.CodeBlock;
import java.util.ArrayList;
import xtc.tree.Node;
import xtc.tree.GNode;
public class JavaForStatement extends JavaStatement{
	public JavaForStatement(JavaScope scope, GNode n){
		super(scope, n);
System.out.println("you have a basic for control: "+((JavaBasicForControl)header).printMe());
	}
	public JavaScope visitBasicForControl(GNode n){
		return new JavaBasicForControl(this,n);
	}
	public CodeBlock printMe(CodeBlock block){
		return block.block("for ("+ ((JavaBasicForControl)header).printMe() + ")",true).attach((CodeBlock)b.get(0)).close();
	}
}