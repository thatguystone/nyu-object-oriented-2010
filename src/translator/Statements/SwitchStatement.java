package translator.Statements;
import translator.JavaScope;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;
import xtc.tree.Node;
import xtc.tree.GNode;
import java.util.ArrayList;
public class SwitchStatement extends JavaStatement{
	private CodeBlock block;  /** the outer block **/
	public SwitchStatement(JavaScope scope, GNode n){
		super(scope, n);
	}
	public void print(CodeBlock b){
		int s=this.node.size();
		this.block=b=b.block("");   /** the outer block for holding all fields belong to switch statement...**/
		CodeBlock temp=new CodeBlock("switch ("+ ((JavaExpression)this.dispatch((GNode)this.node.get(0))).print() + ")");
		/** fields declared inside the clauses had to be put on top of all clauses in order to avoid the "cross initialization" error in c++
		    this change, however, will not affect well-formed Java code? **/
//System.out.println("switch ("+ ((JavaExpression)this.dispatch((GNode)this.node.get(0))).print() + ")");
		for (int i=1;i<s;++i){
			//b.attach((CodeBlock)dispatch((GNode)this.node.get(i)));
			temp.attach((CodeBlock)dispatch((GNode)this.node.get(i)));
		}
		temp.close(false);
		b.attach(temp).close();
	}
	public CodeBlock visitCaseClause(GNode n){
		CodeBlock ret=new CodeBlock().block("case "+((JavaExpression)dispatch((GNode)n.get(0))).print()+":",false);
//System.out.println("case");
		return handleClause(ret,n,1).close(false);
	}
	public CodeBlock visitDefaultClause(GNode n){
//System.out.println("default");
		CodeBlock ret=new CodeBlock().block("default:",false);
		return handleClause(ret,n,0).close(false);
	}
	private CodeBlock handleClause(CodeBlock b,GNode n,int first){
		for (int i=first;i<n.size();++i){
			Object o=n.get(i);
			if (o instanceof Node){
				Object s=dispatch((GNode)o);
				if (s!=null){
					((JavaStatement)s).print(b);
				}else {
					this.dumpNewFields(block);
				}
//System.out.println("you have a statement");
			}else{
				System.out.println("help: child is not a node");
			}
		}
		return b;
	}
}
