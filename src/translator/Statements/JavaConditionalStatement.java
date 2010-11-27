package translator.Statements;

import translator.JavaScope;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;
import xtc.tree.Node;
import xtc.tree.GNode;
import java.util.ArrayList;
class JavaConditionalStatement extends JavaStatement{
	JavaConditionalStatement(JavaScope scope, GNode n){
		super(scope, n);
	}
	public CodeBlock printMe(CodeBlock block){
		int s=b.size()-1;
		CodeBlock ret=block.block("if ("+ ((JavaExpression)header).printMe() + ")",true);
		ret.attach((CodeBlock)b.get(0));
		if (s>0){
			for (int i=1;i<s;++i){
				ret.block("else ").attach((CodeBlock)b.get(i));  /** the "else if" branch(es) (space required between "else" and "if") **/
			}
			ret.block("else").attach((CodeBlock)b.get(s));	/** the else branch **/
		}
		return ret.close();
        }
}
