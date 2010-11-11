package translator;

import translator.Printer.CodeBlock;
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
	private JavaMethodSignature sig;
	
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
		//yes java, we know you don't create fields in classes until after
		//the constructors are done. F-you, too.
		this.sig = new JavaMethodSignature();
		
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
		return this.isStatic() || this.isVisible(Visibility.PRIVATE);
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
	 * Comparing Methods
	 */
	
	/**
	 * Checks to see if this method is equal to another based on its signature.
	 * Note: method name is not a factor of this comparison, only the signature matters.
	 */
	public boolean equals(JavaMethod m) {
		return this.equals(m.getSignature());
	}
	
	/**
	 * Checks to see if this method is equal to another based on its signature.
	 * Note: method name is not a factor of this comparison, only the signature matters.
	 */
	public boolean equals(JavaMethodSignature sig) {
		return this.sig.equals(sig);
	}
	
	/**
	 * Determines if this method can take the parameters of the given method.
	 * Note: method name is not a factor of this comparison, only the signature matters.
	 */
	public boolean canBeUsedAs(JavaMethod m) {
		return this.canBeUsedAs(m.getSignature());
	}
	
	/**
	 * Determines if this method can take the parameters given in the signature.
	 * Note: method name is not a factor of this comparison, only the signature matters.
	 *
	 * This is just a higher-level accessor that accesses this.getSignature() and passes off the compare.
	 * Using this method is preferable to going directly to the signature.
	 */
	public boolean canBeUsedAs(JavaMethodSignature sig) {
		return this.getSignature().canBeUsedAs(sig);
	}
	
	/**
	 * ==================================================================================================
	 * Printing methods
	 */
	
	/**
	 * Prints the method signature to the class definition in the header, if the method is part of
	 * the class.
	 */
	public void printToClassDefinition(CodeBlock b, JavaClass cls) {
		//we only wany to print
		if (cls != this.getJavaClass())
			return;
		
		b.pln("static " + this.getName() + ";");
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
	 * Gets the signature for the method.  Be nice, don't modify it.
	 */
	public JavaMethodSignature getSignature() {
		return this.sig;
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
		this.sig.add(JavaType.getType(this, ((GNode)((GNode)n.get(1)).get(0)).get(0).toString()), this);
	}
}
