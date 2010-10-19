package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 *  This is a simple while loop with a relational expression and a block of code
 */
class ExpWhileStatement extends JavaExpression 
{
    JavaExpression relation;

    JavaExpression block;

    ExpWhileStatement(JavaScope parent, Node n)
    {
	this.node = n;
	this.setScope(parent);
	this.dispatch(this.node); 
	this.relation = (JavaExpression)this.myExpressions.get(0);
	this.block = (JavaExpression)this.myExpressions.get(1);
    }

    //Not sure how we should get {} formatted correctly(if we want to)
    //JavaBlock could handle the {}.  I'm leaving them in for now
    public String printMe()
    {
	return "while(" + this.relation.printMe() + ") {\n" + this.block.printMe() + "}";
    }
}