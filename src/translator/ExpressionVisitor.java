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

	public void visitEqualityExpression(GNode n) {
		myExpressions.add(new ExpTwoPartExp(this, n));
	}

	public void visitConditionalExpression(GNode n) {
		myExpressions.add(new ExpConditionalExpression(this, n));
	}

	public void visitNullLiteral(GNode n){
		myExpressions.add(new ExpNullLiteral(n));
	}

	public void visitBooleanLiteral(GNode n){
		myExpressions.add(new ExpLiteral(n));
	}

	public void visitIntegerLiteral(GNode n) {
		myExpressions.add(new ExpLiteral(n));
	}

	public void visitCharacterLiteral(GNode n){
		myExpressions.add(new ExpLiteral(n));
	}

	public void visitPrimaryIdentifier(GNode n) {
		myExpressions.add(new ExpIdentifier(this, n));
	}

	public void visitQualifiedIdentifier(GNode n) {
		myExpressions.add(new ExpIdentifier(this, n));
	}

	public void visitThisExpression(GNode n) {
		myExpressions.add(new ExpThisExpression(n));
	}

	public void visitStringLiteral(GNode n) {
		myExpressions.add(new ExpStringLiteral(n));
	}

	public void visitArrayInitializer(GNode n) {
		myExpressions.add(new ExpArrayInitializer(this, n));
	}

	public void visitnewArrayExpression(GNode n) {
		myExpressions.add(new ExpnewArrayExpression(this, n));
	}

	public void visitNewClassExpression(GNode n) {
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

	public void visitBasicCastExpression(GNode n) {
		myExpressions.add(new ExpBasicCastExpression(this, n));
	}

	public void visitSelectionExpression(GNode n) {
		myExpressions.add(new ExpSelectionExpression(this, n));
	}
}

