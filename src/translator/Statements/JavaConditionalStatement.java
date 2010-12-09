package translator.Statements;

import translator.JavaScope;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;
import xtc.tree.Node;
import xtc.tree.GNode;
import java.util.ArrayList;
public class JavaConditionalStatement extends JavaStatement{
	JavaConditionalStatement elif;
	public JavaConditionalStatement(JavaScope scope, GNode n){
		super(scope, n);
//CodeBlock test=new CodeBlock();
//this.printMe(test);
	}
	protected void onInstantiate(GNode n){
//System.out.println("you have a conditional statement");
		super.onInstantiate(n);
		if (n.size()>2){	/** if there is an else/else if branch **/
			final GNode g=(GNode)n.get(2);
			if (g!=null){
				final Object o=dispatch(g);
				if (o!=null){
				/** "else if" and "else" need to be handled slightly differently **/
				/** note the debugging messages are NOT printed in sequential order and have to be read from outside to inside **/
					if (o instanceof JavaConditionalStatement){
//System.out.println("you have an else if branch");
						elif=(JavaConditionalStatement)o;
					}else if (o instanceof CodeBlock){
//System.out.println("you have an else branch");
						b.add((CodeBlock)o);
					}else{
//System.out.println("you have an else branch");
                                        /** if it is blockless, then we might just want to add a block around it in our translation
                                        (and we might want to find a better way other than an instanceof test...) **/
                                        //b.add(((JavaStatement)o).printMe(new CodeBlock())
						CodeBlock block=new CodeBlock();
						((JavaStatement)o).print(block);
						b.add(block);   //maybe?
					}
				}
			}
		}
	}
	public CodeBlock printMe(CodeBlock block){
		CodeBlock ret=block.block("if ("+ ((JavaExpression)header).print() + ")",true);
		ret.attach((CodeBlock)b.get(0));
		if (elif!=null){
			elif.printMe(ret.block("else "));
		}else if(b.size()>1){
			ret.attach((CodeBlock)b.get(1));
		}
		return ret.close();
        }
}
