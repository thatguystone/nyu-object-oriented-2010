package translator;

import translator.Printer.CodeBlock;
import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JavaClass extends ActivatableVisitor implements Nameable, Typed {
	/**
	 * The name of the class (not fully qualified, just the name).
	 */
	private String name;
	
	/**
	 * The parent class.  This is going to be something from "extends" or java.lang.Object.
	 */
	private JavaClass parent;
	
	/**
	 * List of all virtual methods in this class (v is for virtual).
	 * Method name -> Method object
	 */
	private LinkedHashMap<String, ArrayList<JavaMethod>> methods = new LinkedHashMap<String, ArrayList<JavaMethod>>();
	
	/**
	 * SAEKJFA;WIE JF K;LSDFJ ASILD JFASD;IFJ!!!!!!! WHY DOES JAVA NOT INHERIT CONSTRUCTORS?!?!?!?!?!?!?!?!?!??!
	 * This feels so dirty and wrong.
	 */
	JavaClass(JavaScope scope, GNode n) {
		super(scope, n);
	}
	
	/**
	 * There are a few minor details we need to sort out once we can access our GNode.
	 */
	protected void onNodeSetup() {
		//we're going to need our name, no matter what
		this.name = this.node.get(1).toString();
		
		//setup our visibility -- we need this from the start so that imports work even
		//when a class hasn't been activated yet
		this.setupVisibility((GNode)this.node.get(0));
		
		//make sure we get added to the class registry
		JavaStatic.pkgs.addClass(this);
	}
	
	public void wrapUp() {
		for (ArrayList<JavaMethod> a : this.methods.values()) {
			for (JavaMethod m : a) {
				System.out.println("JavaClass.wrapUp: " + m.getName());
				m.activate();
			}
		}
	}
	
	/**
	 * Only to be used upon activation.  Does everything we need to get this class ready for translation.
	 */
	protected void process() {
		//go for a nice visit to see everyone
		this.dispatch(this.node);
		
		//activate the parent file so that all the classes with him are included
		this.getJavaFile().activate();
		
		if (JavaStatic.runtime.test("debug"))
			System.out.println("Class activated: " + this.getName());
		
		//check if we have a parent; if we don't, then java.lang.Object is our parent
		this.setParent("java.lang.Object");
		
		//once we're sure we have a parent, then add all our inherited methods
		//and, once the VTable is setup, go ahead and activate the methods
		this.setupVTable();
	}
	
	/**
	 * Prints out the class.
	 *
	 * @param prot The prototype code block.
	 * @param header The header code block.
	 * @param implm The code block for the implementation.
	 */
	public void print(CodeBlock prot, CodeBlock header, CodeBlock implm) {
		this.printPrototype(prot);
		this.printHeader(header);
		this.printImplementation(implm);
	}
	
	/**
	 * Prints out the prototype for this class.
	 */
	private void printPrototype(CodeBlock b) {
		b.pln("class " + this.getName(false) + ";");
	}
	
	/**
	 * Gets that header out.
	 */
	private void printHeader(CodeBlock b) {
		CodeBlock cls = b.block("class " + this.getName(false));
		
		//methods/constructors and fields go here
		
		cls.close();
	}
	
	/**
	 * Gets the methods all printed.
	 */
	private void printImplementation(CodeBlock b) {
		b.pln("METHODS AND FIELD INITIALIZATIONS HERE");
	}
	
	/**
	 * Gets the method from its signature. If overloaded, finds the most-like method to return.
	 */
	public JavaMethod getMethod(String name, JavaMethodSignature sig) {
		
		return null;
	}

	/**
	 * Setup our parent.  Can only be run once, then everything is permanent.
	 */
	private void setParent(String parent) {
		//java.lang.Object has no parent
		if (this.getName(true).equals("java.lang.Object"))
			return;
		
		//only allow one parent to be set
		if (this.parent == null) {
			if (JavaStatic.runtime.test("debug"))
				System.out.println(this.getName() + " extends " + this.getJavaFile().getImport(parent).getName());
			
			//set our parent from its name in import
			this.parent = this.getJavaFile().getImport(parent);
			
			//with the extension, we need to activate it (ie. process it) before we can use it
			this.parent.activate();
		}
	}

	public JavaClass getParent() {
		return this.parent;
	}

	/**
	 * Go through all the parents and get their virtual methods, then just add them.  To do this, we test
	 * if we first have a parent (java.lang.Object doesn't); if we have a parent, grab his virtual methods,
	 * see if we override them (they'll be set in either this.pMethods or this.vMethods), if we don't, then add
	 * them, otherwise, ignore.
	 * 
	 * Since we know that there are problems with VTable resolution from parents, we're going to first add our methods
	 * to our vtable and grab our parents, then we're going to activate our methods so that they translate.
	 */
	private void setupVTable() {
		
	}

	/**
	 * ==================================================================================================
	 * Typed Methods
	 */
	
	/**
	 * Gets the type that this class represents.
	 */
	public JavaType getType() {
		return JavaType.getType(this.getName());
	} 
	
	/**
	 * ==================================================================================================
	 * Nameable Methods
	 */
	
	/**
	 * Gets the fully qualified java name.
	 */
	public String getName() {
		return this.getName(true);
	}
	
	/**
	 * Gets the java name.
	 *
	 * @param fullName True for the fully-qualified java name; false for just the last part of the name.
	 */
	public String getName(boolean fullName) {
		String name = "";
		if (fullName)
			name += this.getPackageName() + ".";
		
		return name + this.name; 
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	 
	/**
	 * Handles resolving dependencies for inheritance.  When it sees an extension, it throws the name
	 * to its parent file's import manager in order to resolve the name and activate the class so that 
	 * it can extend it properly.
	 */
	public void visitExtension(GNode n) {
		//java only supports single inheritance...no need for loops or anything here
		this.setParent((String)((GNode)((GNode)n.get(0)).get(0)).get(0));
	}
	
	/**
	 * Take in a method.  Adds the method to our method table.
	 */
	public void visitMethodDeclaration(GNode n) {
		JavaMethod m = new JavaMethod(this, n);
		String name = m.getName();
		
		if (!this.methods.containsKey(name))
			this.methods.put(name, new ArrayList<JavaMethod>());
		
		this.methods.get(name).add(m);
	}
	
	/**
	 * Create a FieldDec object, the FieldDec will handle everything else so this is all we need to do.
	 */
	public void visitFieldDeclaration(GNode n) {
		FieldDec f = new FieldDec(this, n);
	}
	
	/**
	 * We process modifiers on instantiation, so skip that here.
	 */
	public void visitModifiers(GNode n) { }
	
	public void visitConstructorDeclaration(GNode n) {
		//special...yay :(
	}
}
