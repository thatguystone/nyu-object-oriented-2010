package translator;

import xtc.tree.GNode;

/**
 * A representation of a java method.
 *
 * For activation, this guy does things a little differently than everything else.  When a Method is created,
 * it takes the Block GNode and sets it to be the node that is visited on activation, and it visits on all the 
 * other nodes of the GNode on instantiation.  In this way, we can get all information about the method by doing
 * minimal visiting on creation, and when activated, we will translate the Block that we saved.
 */ 
public class JavaMethod extends ActivatableVisitor implements Nameable, Typed {
	/**
	 * Our overloadable method name.
	 */
	private String name;

	/**
	 * Our method signature.
	 */
	private String signature;
	
	/**
	 * The type this method returns.
	 */
	private JavaType returnType;
	
	/**
	 * SAEKJFA;WIE JF K;LSDFJ ASILD JFASD;IFJ!!!!!!! WHY DOES JAVA NOT INHERIT CONSTRUCTORS?!?!?!?!?!?!?!?!?!??!
	 * This feels so dirty and wrong.
	 */
	JavaMethod(GNode n) {
		super(n);
	}
	JavaMethod(JavaScope scope, GNode n) {
		super(scope, n);
	}
	
	/**
	 * There are a few minor details we need to sort out once we can access our GNode.
	 */
	protected void onNodeSetup() {
		//the nodes that we are going to visit NOW
		GNode visitFirst = this.node;
		
		//save our block for visiting later
		this.node = (GNode)this.node.get(7);
		
		//and remove the block from what we're visiting NOW
		visitFirst.set(7, null);
		
		//that name thing
		this.name = visitFirst.get(3).toString();
		
		//and go visit on our basic method info
		this.dispatch(visitFirst);
	}
	
	/**
	 * Determine if we are a virtual method.
	 */
	public boolean isVirtual() {
		return this.isStatic() || this.getVisibility(Visibility.PRIVATE);
	}

	/**
	 * Override processing from ActivatableVisitor.  When we are activated, this is
	 * what we are going to need to run for the translation to take place.
	 */
	protected void process() {
		this.dispatch(this.node);
	}

	/**
	 * ==================================================================================================
	 * Nameable Methods
	 */
	
	/**
	 * Gets the method name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the java name.
	 *
	 * @param fullName Does nothing.
	 */
	public String getName(boolean fullName) {
		return this.getName();
	}
	
	/**
	 * ==================================================================================================
	 * Typed Methods
	 */
	
	/**
	 * Gets the type that this class represents.
	 */
	public JavaType getType() {
		return this.returnType;
	} 
	
	/**
	 * ==================================================================================================
	 * Visitor Methods -- first shot
	 */
	
	/**
	 * Grab the method return type.
	 */
	public void visitType(GNode n) {
		this.returnType = JavaType.getType(this, ((GNode)n.get(0)).get(0).toString());
	}
	
	/**
	 * Void has its own SPECIAL (as in "herp derp") type.
	 */
	public void visitVoidType(GNode n) {
		this.returnType = JavaType.getType("void");
	}
	
	/**
	 * Grabs the formal parameters and adds them to our signature.
	 */
	public void visitFormalParameters(GNode n) {
		//just call down to our FormalParameter visitor
		this.visit(n);
	}
	
	/**
	 * Visit our formal parameters and add them to our list of fields.
	 * We're using this to take advantage of the scope searching already implemented
	 * in fields.
	 * JavaFieldDec isn't needed here because we can't pass multiple fields with the same type declaration.
	 */
	public void visitFormalParameter(GNode n) {
		
		//System.out.println("Formal parameter in JavaMethod found!!!!!!! I feel....accomplished?");
		
		//JavaField field = new JavaField((String)((GNode)((GNode)n.get(1)).get(0)).get(0), this, this.getFile(), n);
		//this.parameters.put(field.getName(), field);
	}
	
	/**
	 * ==================================================================================================
	 * Visitor Methods -- for Activation
	 */
	public void visitBlock(GNode n) {
		System.out.println(this.getName() + " --> " + this.getJavaClass().getName());
		JavaBlock b = new JavaBlock(this, n);
	}
}
