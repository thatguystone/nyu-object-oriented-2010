/*
package translator.Statements;

import xtc.tree.Node;
import xtc.tree.GNode;
import translator.FieldDec;
import translator.JavaField;

class JavaBasicForControl extends JavaScope{
	JavaScope ForInit;
	JavaScope ForExpression;
	JavaScope ForUpdate;
	JavaBasicForControl (JavaScope scope, Node n) {
		super(scope,n);
	}
	protected void onInstantiate(GNode n){
		if (n.get(3)!=null){
			ForExpression=dispatch((GNode)n.set(3,null));
		}
		if (n.get(4)!=null){
			ForUpdate=dispatch((GNode)n.set(4,null));
		}
		if (n.get(1)!=null){  /** if this node has a type **/
		/*	new FieldDec(this,n){
				public void visitDeclarator(GNode n){
					/** n is now a field dec node with 2 extra nulls appended**/
					/*ForInit=new JavaField(false, this.modifiers, this.type, this.dimensions, this, n);
				}
			};
		}else{  /** otherwise it is a single expression or expression list (assuming JavaScope will add field to parent scope) **/
			/*ForInit=n.dispatch(get(2));
		}
	}
}
*/
