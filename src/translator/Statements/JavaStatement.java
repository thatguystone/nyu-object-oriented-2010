
package translator.Statements;
import translator.JavaScope;
import translator.Printer.CodeBlock;
import java.util.ArrayList;
import xtc.tree.GNode;
/**
 * Handles anything that can have its own scope, from a File to a Block.
 */

class JavaStatement extends JavaScope {
	/**
	 * SAEKJFA;WIE JF K;LSDFJ ASILD JFASD;IFJ!!!!!!! WHY DOES JAVA NOT INHERIT CONSTRUCTORS?!?!?!?!?!?!?!?!?!??!
	 * This feels so dirty and wrong.
	 */
	JavaScope header;
	ArrayList<CodeBlock> b = new ArrayList<CodeBlock>();
	JavaStatement(JavaScope scope, GNode n){
		super(scope, n);
		header=(JavaScope)dispatch((GNode)n.get(0));
		for (int i=1;i<n.size();++i){
			final GNode g=(GNode)n.get(i);
			if (g!=null){
				Object o=dispatch(g);
				if (o instanceof CodeBlock){
					b.add((CodeBlock)o);
				}else{
					/** if it is blockless, then we might just want to add a block around it in our translation
                                        (and we might want to find a better way other than an instanceof test...) **/
					b.add(((JavaStatement)o).printMe(new CodeBlock()));
				}
			}
		}
	}
	CodeBlock printMe(CodeBlock block){
		return null;
	}
}

