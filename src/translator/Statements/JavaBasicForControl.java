package translator.Statements;
import xtc.tree.Node;
import xtc.tree.GNode;
import translator.FieldDec;
import translator.JavaField;
import translator.JavaScope;
import translator.Expressions.JavaExpression;
import translator.Expressions.ConditionalExpression;
import translator.Printer.CodeBlock;
import java.util.ArrayList;
public class JavaBasicForControl extends JavaScope{
//	JavaExpression ForInit,ForExpression,ForUpdate;
	String ctrl;
	public JavaBasicForControl (JavaScope scope, GNode n) {
		super(scope,n);
	}
	protected void onInstantiate(GNode n){
		final GNode Init=(GNode)n.get(2);
		final GNode Exp=(GNode)n.get(3);
		final GNode Upd=(GNode)n.get(4);
		ctrl="";
		if (n.get(1)!=null){  /** if this node has a type **/
			new FieldDec(this, n);  /** assuming FieldDec adds field to this.field, then the for loop initialization
						    would be just printed as a field, and ForInit would be null and would not be printed**/
			ArrayList<JavaField> d=this.getAllFields();
			ctrl+=d.get(0).printMe();
			for (int i=1,s=d.size();i<s;++i){
				ctrl+=","+d.get(i).printMe();
			}
		}else if (Init!=null){  /** otherwise it is a single expression or expression list (assuming JavaScope will add field to parent scope) **/
			//ForInit=(JavaExpression)dispatch(Init);
			ctrl+=((JavaExpression)dispatch(Init)).print()+";";
		}
		if (Exp!=null){
			//ForExpression=(JavaExpression)dispatch(Exp);
			ctrl+=((JavaExpression)dispatch(Exp)).print();
		}
		ctrl+=";";
		if (Upd!=null){
			//ForUpdate=(JavaExpression)dispatch(Upd);
			ctrl+=((JavaExpression)dispatch(Upd)).print();
		}
	}
	public String printMe(){
		return ctrl;
	}
}
