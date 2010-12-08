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
	JavaExpression ForInit,ForExpression,ForUpdate;
	public JavaBasicForControl (JavaScope scope, GNode n) {
		super(scope,n);
	}
	protected void onInstantiate(GNode n){
		
		if (n.get(3)!=null){
			ForExpression=(JavaExpression)dispatch((GNode)n.set(3,null));
		}
		if (n.get(4)!=null){
			ForUpdate=(JavaExpression)dispatch((GNode)n.set(4,null));
		}
		if (n.get(1)!=null){  /** if this node has a type **/
			new FieldDec(this, n);  /** assuming FieldDec adds field to this.field, then the for loop initialization
						    would be just printed as a field, and ForInit would be null and would not be printed**/
		}else{  /** otherwise it is a single expression or expression list (assuming JavaScope will add field to parent scope) **/
			ForInit=(JavaExpression)dispatch((GNode)n.get(2));
		}
	}
	public String printMe(){
		if (ForInit!=null){
			return ForInit.print()+";"+ForExpression.print()+";"+ForUpdate.print();
		}
		ArrayList<JavaField> d=this.getAllFields();
		int s=d.size();
		JavaField first=d.get(0);
		String init=first.printMe();
		for (int i=1;i<s;++i){
			init+=","+d.get(i).printMe();
		}
		//System.out.println("initialization: "+init);   the initialization is printing correctly
//		System.out.println(ForExpression == null);
//System.out.println(ForUpdate.toString());
		return init+";"+ForExpression.print()+";"+ForUpdate.print();
	}
}