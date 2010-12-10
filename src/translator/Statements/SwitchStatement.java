package translator.Statements;
import translator.JavaScope;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;
import xtc.tree.Node;
import xtc.tree.GNode;
import java.util.ArrayList;
public class SwitchStatement extends JavaStatement{
	public SwitchStatement(JavaScope scope, GNode n){
		super(scope, n);
	}
	public void print(CodeBlock b){
		int s=this.node.size();
		b=b.block("switch ("+ ((JavaExpression)this.dispatch((GNode)this.node.get(0))).print() + ")",true);
//System.out.println("switch ("+ ((JavaExpression)this.dispatch((GNode)this.node.get(0))).print() + ")");
		for (int i=1;i<s;++i){
			b.attach((CodeBlock)dispatch((GNode)this.node.get(i)));
		}
		b.close();
	}
	public CodeBlock visitCaseClause(GNode n){
		CodeBlock ret=new CodeBlock().block("case "+((JavaExpression)dispatch((GNode)n.get(0))).print()+":",false);
//System.out.println("case");
		return handleClause(ret,n,1).close();
	}
	public CodeBlock visitDefaultClause(GNode n){
//System.out.println("default");
		CodeBlock ret=new CodeBlock().block("default:",false);
		return handleClause(ret,n,0).close();
	}
	private CodeBlock handleClause(CodeBlock b,GNode n,int first){
		for (int i=first;i<n.size();++i){
			Object o=n.get(i);
			if (o instanceof Node){
				((JavaStatement)dispatch((GNode)o)).print(b);
//System.out.println("you have a statement");
			}else{
				System.out.println("help: child is not a node");
			}
		}
		return b;
	}
}
