package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 *  This is a do while statement, same as the while statement but block and relational expression have switched locations
 */
class ExpDoWhileStatement extends JavaExpression
{
    JavaExpression block;
    
    JavaExpression relation;

    ExpDoWhileStatement(JavaScope parent, Node n)
    {
	this.node = n;
	this.setScope(parent);
	this.dispatch(this.node);
	this.block = (JavaExpression)myExpressions.get(0);
	this.relation = (JavaExpression)myExpressions.get(1);
    }
   
    //Like the WhileStatement, there is the problem of formatting the {} correctly and also the ending while(relation); if we want to get the indentation looking  nice
    public String printMe()
    {
	return "do {\n" + this.block.printMe() + "} " + "while(" + this.relation.printMe() + ");";
    }

}