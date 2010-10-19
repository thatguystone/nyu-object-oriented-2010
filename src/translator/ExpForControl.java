package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;
/**
 * This is the conditional statement within a for statement.
 * e.g. for(ForControl).  It's made up of a variable declaration, relationalexpression, and an expression list 
 */
class ExpForControl extends JavaExpression
{
    //I'm still figuring out how to handle the field declaration within the forcontrol block
 
    JavaExpression relation;
    
    JavaExpression ExpList;

    ExpForControl(JavaScope parent, Node n)
    {
	this.node = n;
	this.setScope(parent);
	this.dispatch(n);
	this.ConVariable = new JavaVariable(this.parent, n);
	this.relation = (JavaExpression)this.myExpressions.get(4);
	this.ExpList = (JavaExpression)this.myExpressions.get(5);
    }

    public String printMe()
    {
	return "Variable;" + this.relation.printMe() + ";" + this.ExpList.printMe();
    }
}