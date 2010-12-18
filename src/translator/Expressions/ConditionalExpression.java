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
	JavaExpression[] expressions;// = new JavaExpression[3];
	/** the index of the expression that requires a cast **/
	private int c;
	public ConditionalExpression (JavaScope scope, GNode n) {
		super(scope, n);
	}
	protected void onInstantiate(GNode n) {
		JavaType t1,t2;
		expressions=new JavaExpression[3];
		expressions[0] = (JavaExpression)this.dispatch((GNode)n.get(0));
		expressions[1] = (JavaExpression)this.dispatch((GNode)n.get(1));
		expressions[2] = (JavaExpression)this.dispatch((GNode)n.get(2));
		t1=expressions[1].getType();
		t2=expressions[2].getType();
		/** if 2 types are exactly the same, then we do not need cast, and we probably do not need to bother with primitives **/
		if (t1.isPrimitive()||t1==t2){
			this.setType(t1);
			c=0;  //cast is not needed
		}else if (t1.isChildOf(t2)){
			/** assume the Java code is well-formed (i.e. the 2 types must be related)**/
			this.setType(t2);
			c=1;  //1st expression requires a cast
		}else{
			this.setType(t1);
			c=2;  //2nd expression requires a cast 
		}
	}

	public String print(){
		return "(" + expressions[0].print() + "?" + (c==0 ? (expressions[1].print() + ":" + expressions[2].print()) : (c==1 ? 
		("__rt::javaCast<" + this.returnType.getCppName() + ">(" + expressions[1].print() + ")" + ":" + expressions[2].print()) :
		(expressions[1].print() + ":" + "__rt::javaCast<" + this.returnType.getCppName() + ">(" + expressions[2].print() + ")")))  + ")";
		//lol that was fun...
	}

}
