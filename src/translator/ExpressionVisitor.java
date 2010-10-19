package translator;

import java.util.ArrayList;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
/**
 * Visits expressions, who knew?
 * Anything that needs to visit expressions extends this class.
 */
abstract class ExpressionVisitor extends JavaScope {

	protected ArrayList<JavaExpression> myExpressions = new ArrayList<JavaExpression>();

	protected Node node;

	/**
	 * You may notice a trend with these functions
	 * More to come in future versions!
	 */
	public void visitAdditiveExpression(GNode n) {
		myExpressions.add(new ExpTwoPartExp(this, n));
	}

	public void visitMultiplicativeExpression(GNode n) {
		myExpressions.add(new ExpTwoPartExp(this, n));
	}

	public void visitRelationalExpression(GNode n) {
		myExpressions.add(new ExpTwoPartExp(this, n));
	}

	public void visitConditionalExpression(GNode n) {
		myExpressions.add(new ExpConditionalExpression(this, n));
	}

	public void visitIntegerLiteral(GNode n) {
		myExpressions.add(new ExpIdentifier(n));
	}

	public void visitQualifiedIdentifier(GNode n) {
		myExpressions.add(new ExpIdentifier(n));
	}

	public void visitArrayInitializer(GNode n) {
		myExpressions.add(new ExpArrayInitializer(this, n));
	}

	public void visitnewArrayExpression(GNode n) {
		myExpressions.add(new ExpnewArrayExpression(this, n));
	}

	public void visitnewClassExpression(GNode n) {
		myExpressions.add(new ExpnewClassExpression(this, n));
	}

	public void visitExpression(GNode n) {
		myExpressions.add(new ExpAssignmentExpression(this, n));
	}

	public void visitCallExpression(GNode n) {
		myExpressions.add(new ExpCallExpression(this, n));
	}

	public void visitCastExpression(GNode n) {
		myExpressions.add(new ExpCastExpression(this, n));
	}

    	public void visitForStatement(GNode n) {
    		myExpressions.add(new ExpForStatement(this, n));
    	}

    	public void visitWhileStatement(GNode n) {
    		myExpressions.add(new ExpWhileStatement(this, n));
    	}

    	public void visitDoWhileStatement(GNode n) {
   		myExpressions.add(new ExpDoWhileStatement(this, n));
    	}
}
