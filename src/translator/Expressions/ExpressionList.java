package translator.Expressions;
import translator.JavaScope;
import java.util.ArrayList;
import xtc.tree.GNode;
/**
 * this class was added for organizational purposes
 */
public class ExpressionList extends JavaExpression{
	ArrayList<JavaExpression> expressions = new ArrayList<JavaExpression>();
	public ExpressionList (JavaScope scope, GNode n){
		super(scope, n);
	}
	protected void onInstantiate(GNode n){
		for (Object m:n){
			expressions.add((JavaExpression)this.dispatch((GNode)m));
		}
	}
	public String printMe() {
		int n=expressions.size();
		String ret="";
		for (int i=0;i<n-1;++i){
			ret+=((JavaExpression)expressions.get(0)).printMe()+",";
		}
		return ret+((JavaExpression)expressions.get(n-1)).printMe();
	}
}
