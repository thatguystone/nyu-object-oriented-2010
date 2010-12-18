package translator.Expressions;

import translator.JavaType;
import translator.JavaScope;
import xtc.tree.GNode;

/**
 * A conditional expression.
 */
public class ConditionalExpression extends JavaExpression {

	/**
	 * The 3 expressions that make up this conditional expression
	 */
	JavaExpression[] expressions;

	public ConditionalExpression (JavaScope scope, GNode n) {
		super(scope, n);
	}
	protected void onInstantiate(GNode n) {
		JavaType t1,t2;
		expressions=new JavaExpression[3];
		expressions[0] = (JavaExpression)this.dispatch((GNode)n.get(0));
		expressions[1] = (JavaExpression)this.dispatch((GNode)n.get(1));
		expressions[2] = (JavaExpression)this.dispatch((GNode)n.get(2));

		if (expressions[1].getType().isChildOf(expressions[2].getType())){
			this.setType(expressions[2].getType());
		}else{
			this.setType(expressions[1].getType());
		}
	}

	public String print(){
		return "(" + expressions[0].print() + "?" + this.format(expressions[1]) + ":" + this.format(expressions[2]) + ")";
	}

	public String format(JavaExpression exp) {
		String temp = "";
		if (!(exp.getType().isPrimitive()) 
			|| !(exp.getType().getName().equals("java.lang.string")) 
			|| !(exp.getType() == this.getType()))
			temp += "(" + this.getType().getCppName() + ")";
		return temp + exp.print();
	}

}
