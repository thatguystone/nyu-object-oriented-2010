package translator.Expressions;

import translator.JavaScope;
import translator.JavaType;
import translator.JavaClass;
import translator.Typed;
import xtc.tree.GNode;

/**
 * Just to use as a type identifier
 */
abstract public class JavaExpression extends JavaScope implements Typed {
	/**
	 * The return type of this expression.
	 */
	protected JavaType returnType;
	
	/**
	 * If our type is being used in a static context.
	 */
	private boolean isTypeStatic = false;
	
	public JavaExpression(JavaScope scope, GNode n)	{
		super(scope, n);
	}

	/**
	 * Get the type that this expression returns once everything inside it is done.
	 */
	public JavaType getType() {
		return this.returnType;
	}

	/**
	 * Set the return type of this expression.
	 */
	public void setType(JavaType type) {
		this.setType(type, false);
	}
	
	/**
	 * Set the return type of this expression.
	 */
	public void setType(JavaType type, boolean isStatic) {
		this.returnType = type;
		this.isTypeStatic = isStatic;
	}

	public boolean isTypeStatic() {
		return this.isTypeStatic;
	}
	
	/**
	 * The print method...really, it returns a string to be printed.
	 */
	public abstract String print();
	
	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
	/*
	public JavaExpression visitCallExpression(GNode n) {
		return new CallExpression(this, n);
	}
	
	public JavaExpression visitPrimaryIdentifier(GNode n) {
		return new Identifier(this, n);
	}
	
	/**
	 * You may notice a trend with these functions
	 * More to come in future versions!
	
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
