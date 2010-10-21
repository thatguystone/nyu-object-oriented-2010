package translator;

import java.util.ArrayList;

import xtc.tree.Node;
import xtc.tree.Visitor;

class ExpAssignmentExpression extends JavaExpression {

	String operator;

	JavaExpression first;

	JavaExpression second;

	ExpAssignmentExpression(JavaScope parent, Node n) {
		this.node = n;
		this.setScope(parent);
		this.operator = (String)n.get(1);
		this.visit(this.node);
		this.first = (JavaExpression)this.myExpressions.get(0);
		this.second = (JavaExpression)this.myExpressions.get(1);
		
	}

	public String printMe() {
		String ret = "";
		if (this.getScope().getCls().getField(first.printMe()) != null)
			ret += "__this->";
		
		ret += first.printMe();
		
		ret += this.operator;
		
		//this is going to need to be updated to take into account local assignment overriding parent / class assignment.
		if (this.getScope().getField(second.printMe()) == null) {
			if (this.getScope().getCls().getField(second.printMe()) != null)
				ret += "__this->";
		}
		
		ret += second.printMe();
		
		return ret;
	}
}
