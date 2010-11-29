package translator.Statements;
import translator.JavaScope;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;
import xtc.tree.Node;
import xtc.tree.GNode;
import java.util.ArrayList;
class JavaSwitchStatement extends JavaStatement{
	JavaSwitchStatement(JavaScope scope, GNode n){
		super(scope, n);
	}
	public CodeBlock printMe(CodeBlock block){
		int s=b.size()-1;
		CodeBlock ret=block.block("switch ("+ ((JavaExpression)header).printMe() + ")",true);
		ret.attach((CodeBlock)b.get(0));
		if (s>0){
			for (int i=1;i<s;++i){
				ret.block("else ").attach((CodeBlock)b.get(i));  /** the "else if" branch(es) (space required between "else" and "if") **/
			}
			ret.block("else").attach((CodeBlock)b.get(s));	/** the else branch **/
		}
		return ret.close();
        }
	public CodeBlock visitCaseClause(GNode n){
		CodeBlock ret=new CodeBlock().block("case "+((JavaExpression)dispatch((GNode)n.get(0))).printMe()+":",false);
		for (int i=1;i<n.size();++i){
			Object o=n.get(1);
			if (o instanceof Node){
				ret.attach((CodeBlock)dispatch((Node)o));
			}
		}
		return ret.close();
	}
/*
	public CodeBlock visitDefaultClause(GNode n){
		CodeBlock ret=new CodeBlock().block("case "+((JavaExpression)dispatch((GNode)n.get(0))).printMe()+":",false);
		for (int i=1;i<n.size();++i){
			Object o=n.get(1);
			if (o instanceof Node){
				ret.attach((CodeBlock)dispatch((Node)o));
			}
		}
		return ret.close();
	}
*/
}
