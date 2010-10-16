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
		myExpressions.add(new ExpTwoPartExp(n));
	}

	public void visitMultiplicativeExpression(GNode n) {
		myExpressions.add(new ExpTwoPartExp(n));
	}

	public void visitConditionalExpression(GNode n) {
		myExpressions.add(new ExpTwoPartExp(n));
	}

	public void visitIntegerLiteral(GNode n) {
		myExpressions.add(new ExpIdentifier(n));
	}

	public void visitQualifiedIdentifier(GNode n) {
		myExpressions.add(new ExpIdentifier(n));
	}

	public void visitArrayInitializer(GNode n) {
		myExpressions.add(new ExpArrayInitializer(n));
	}

	public void visitnewArrayExpression(GNode n) {
		myExpressions.add(new ExpnewArrayExpression(n));
	}

	public void visitnewClassExpression(GNode n) {
		myExpressions.add(new ExpnewClassExpression(n));
	}

	public void visitExpression(GNode n) {
		myExpressions.add(new ExpAssignmentExpression(n));
	}
}
