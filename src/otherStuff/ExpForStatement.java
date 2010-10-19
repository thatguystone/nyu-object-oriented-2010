package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

class ExpForStatement extends JavaExpression
{
    JavaExpression ForControl;
    
    JavaExpression Block;

    ExpForStatement(JavaScope parent, Node n)
    {
	this.node = n;
	this.setScope(parent);
	this.dispatch(n);
	this.ForControl = (JavaExpression)this.myExpressions.get(0);
	this.Block = (JavaExpression)this.myExpressions.get(1);
    }

    public String printMe()
    {
	return "For";
    }
}