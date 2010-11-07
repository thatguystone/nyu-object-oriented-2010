package translator.Expressions;

import translator.*;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
/**
 * Visits expressions, who knew?
 * Anything that needs to visit expressions extends this class.
 */
abstract public class ExpressionVisitor extends JavaScope {

	ExpressionVisitor(JavaScope scope, Node n) {
		super(scope, (GNode)n);
	}

	/**
	 * Expression Visitor doesn't know what to do with the newly created expression so send it
	 * to the class that does!
	 */
	abstract public void processExpression(JavaExpression expression);

	/**
	 * You may notice a trend with these functions
	 * More to come in future versions!
	 */
	/*
	public void visitAdditiveExpression(GNode n) {
		this.processExpression(new TwoPartExp(this, n));
	}

	public void visitMultiplicativeExpression(GNode n) {
		this.processExpression(new TwoPartExp(this, n));
	}

	public void visitRelationalExpression(GNode n) {
		this.processExpression(new TwoPartExp(this, n));
	}

	public void visitEqualityExpression(GNode n) {
		this.processExpression(new TwoPartExp(this, n));
	}

	public void visitConditionalExpression(GNode n) {
		this.processExpression(new ConditionalExpression(this, n));
	}

	public void visitNullLiteral(GNode n){
		this.processExpression(new NullLiteral(n));
	}

	public void visitBooleanLiteral(GNode n){
		this.processExpression(new Literal(n));
	}

	public void visitIntegerLiteral(GNode n) {
		this.processExpression(new Literal(n));
	}

	public void visitCharacterLiteral(GNode n){
		this.processExpression(new Literal(n));
	}

	public void visitPrimaryIdentifier(GNode n) {
		this.processExpression(new Identifier(this, n));
	}

	public void visitQualifiedIdentifier(GNode n) {
		this.processExpression(new Identifier(this, n));
	}

	public void visitThisExpression(GNode n) {
		this.processExpression(new ThisExpression(this, n));
	}

	public void visitStringLiteral(GNode n) {
		this.processExpression(new Literal(n));
	}

	public void visitArrayInitializer(GNode n) {
		this.processExpression(new ArrayInitializer(this, n));
	}

	public void visitNewArrayExpression(GNode n) {
		this.processExpression(new newArrayExpression(this, n));
	}

	public void visitNewClassExpression(GNode n) {
		this.processExpression(new newClassExpression(this, n));
	}

	public void visitExpression(GNode n) {
		this.processExpression(new AssignmentExpression(this, n));
	}

	public void visitCallExpression(GNode n) {
		this.processExpression(new CallExpression(this, n));
	}

	public void visitCastExpression(GNode n) {
		this.processExpression(new CastExpression(this, n));
	}

	public void visitBasicCastExpression(GNode n) {
		this.processExpression(new BasicCastExpression(this, n));
	}

	public void visitSelectionExpression(GNode n) {
		this.processExpression(new SelectionExpression(this, n));
	}
	
	public void visitPostfixExpression(GNode n) {
		this.processExpression(new PostfixExpression(this, n));
	}
	
	public void visitUnaryExpression(GNode n) {
		this.processExpression(new UnaryExpression(this, n));
	}
	*/
}

