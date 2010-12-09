package translator.Expressions;
import translator.JavaScope;
import java.util.ArrayList;
import xtc.tree.GNode;
/**
 * this class was added for organizational purposes
 */
public class ExpressionList extends JavaExpression{
	ArrayList<JavaExpression> expressions;
	public ExpressionList (JavaScope scope, GNode n){
		super(scope, n);
	}
	protected void onInstantiate(GNode n){
		expressions = new ArrayList<JavaExpression>();
		for (Object m:n){
			expressions.add((JavaExpression)dispatch((GNode)m));
		}
	}
	public String print() {
		int n=expressions.size();
		String ret="";
		for (int i=0;i<n-1;++i){
			ret+=((JavaExpression)expressions.get(0)).print()+",";
		}
		return ret+((JavaExpression)expressions.get(n-1)).print();
	}
}
