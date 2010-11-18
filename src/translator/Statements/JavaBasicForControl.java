
package translator.Statements;

import xtc.tree.Node;
import xtc.tree.GNode;
import translator.FieldDec;
import translator.JavaField;
import translator.JavaScope;
class JavaBasicForControl extends JavaScope{
	JavaScope ForInit;
	JavaScope ForExpression;
	JavaScope ForUpdate;
	JavaBasicForControl (JavaScope scope, GNode n) {
		super(scope,n);
	}
	protected void onInstantiate(GNode n){
		if (n.get(3)!=null){
			ForExpression=(JavaScope)dispatch((GNode)n.set(3,null));
		}
		if (n.get(4)!=null){
			ForUpdate=(JavaScope)dispatch((GNode)n.set(4,null));
		}
		if (n.get(1)!=null){  /** if this node has a type **/
			new FieldDec(this, n);  /** assuming FieldDec adds field to this.field, then the for loop initialization
						    would be just printed as a field, and ForInit would be null and would not be printed**/
		}else{  /** otherwise it is a single expression or expression list (assuming JavaScope will add field to parent scope) **/
			ForInit=(JavaScope)dispatch((GNode)n.get(2));
		}
	}
}

