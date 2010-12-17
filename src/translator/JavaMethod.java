package translator;

import translator.Printer.CodeBlock;
import xtc.tree.GNode;

import java.util.HashSet;
import java.util.LinkedHashMap;

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
	protected String name;
	
	/**
	 * The mangled name of the method.
	 */
	private String mangledName;

	/**
	 * Does this method have chaining?
	 */
	protected boolean chaining = false;

	/**
	 * Our method signature.
	 */
	protected JavaMethodSignature sig;
	
	/**
	 * The type this method returns.
	 */
	protected JavaType returnType;
	
	/**
	 * A quick-and-dirty way to get at constructors.
	 */
	public static class Constructor extends JavaMethod {

		private boolean headerPrinted = false;		

		Constructor(JavaScope s, GNode n) {
			super(s, n);
			this.returnType = JavaType.getType("void");
		}
		
		protected void onNodeSetup() {
			//yes java, we know you don't create fields in classes until after
			//the constructors are done. F-you, too.
			this.sig = new JavaMethodSignature();
		
			//the nodes that we are going to visit NOW
			GNode visitFirst = this.node;
		
			//save our block for visiting later
			this.node = (GNode)this.node.get(5);
		
			//and remove the block from what we're visiting NOW
			visitFirst.set(5, null);
		
			//that name thing
			this.name = visitFirst.get(2).toString();
		
			//and go visit on our basic method info
			this.dispatch(visitFirst);
		}
		
		/**
		 * Prints the implementation of this method.
		 */
		public void print(CodeBlock b, JavaClass cls) {
			//we only want to print to our defining class
			//and make sure we're not native -- that would just be a drag!
			if (cls != this.getJavaClass() || this.isNative())
				return;
	
			//in the future this will also print the sig
			b = b.block(
				"void " + this.getJavaClass().getCppName(false, false) + "::__CONSTRUCTOR__" + this.getCppName(false) + "(" + 
					this.getPrintArguments(true, cls) +
				")"
			);
		
			//Sets a temporary block to hold all the information from our statements.
			//This also "activates" our method. Since this is guaranteed to only happen once, we can
			//probably remove JavaMethod from activatible visitor.
			CodeBlock block = (CodeBlock)this.dispatch(this.node);
			if (this.chaining)
				//only create a chain variable if we need it
				b.pln("java::lang::Object __chain;");
			//now that we know if we need chaining, we can attach the main block
			b.attach(block);
			for (JavaField f : this.getScope().fields.values())
				f.constructorPrint(b);
			b.close();
		}
	
		/**
		 * Prints the method signature to the class definition in the header, if the method is part of
		 * the class.
		 */
		public void printToClassDefinition(CodeBlock b, JavaClass cls) {
			//we only want to print to our defining class
			if (cls != this.getJavaClass() || this.headerPrinted)
				return;
			this.headerPrinted = true;
			
			String constructorName = "__CONSTRUCTOR__" + this.getCppName(false, false);
			
			b = b
				.block("__" + this.getCppName(false, false) + "(" + this.sig.getCppArguments(true) + ") :", false)
					.block("__vptr(&__vtable)")
						.pln(constructorName + "(this" + (this.sig.size() == 0 ? "" : ", " + this.sig.getCppArguments(true, false)) + ");")
					.close()
				.close()
				
				.pln("static void " + constructorName + "(" + this.getPrintArguments(false, cls) + ");")
			;
		}
	
		/**
		 * Prints the method signature to the vTable.
		 */
		public void printToVTable(CodeBlock b, JavaClass cls) {
			b.pln(
				this.returnType.getCppName() +
				" (*" + "__CONSTRUCTOR__" + this.getCppName(false, false) + ")(" + 
					this.getPrintArguments(false, cls) +
				");"
			);
		}
		
		public String getVTableInitialize(JavaClass cls) {
			//cast the vTable entry, do we need to cast our entry?
			String cast = this.getVTableInitializeCast(cls);
		
			return "__CONSTRUCTOR__" + this.getCppName(false, false) + 
				"(" + cast + "&" + this.getJavaClass().getCppName(true, false) + "::__CONSTRUCTOR__" + this.getCppName(false) +")"
			;
		}
		
		protected String getVTableInitializeCast(JavaClass cls) {
			//cast the vTable entry, do we need to cast our entry?
			String cast = "";
		
			if (this.getJavaClass() != cls) {
				cast = this.getSignature().getCppArguments(false);
				cast = "(" + this.getType().getCppName() + "(*)(" + cls.getCppName(true, false) + "*" + (cast.length() > 0 ? ", " + cast : "") + "))";
			}
		
			return cast;
		}
		
		/**
		 * Gets the arguments for printing.
		 */
		protected String getPrintArguments(boolean withNames, JavaClass cls) {
			return this.getPrintArguments(withNames, true, cls);
		}
		 
		protected String getPrintArguments(boolean withNames, boolean withTypes, JavaClass cls) {
			String args = this.sig.getCppArguments(withNames, withTypes);
		
			String __this =
				(withTypes ? cls.getCppName(true, false) + "*" : "") +
				(withNames && withTypes ? " " : "") +
				(withNames ? " __this" : "") + (args.length() > 0 ? ", " : "")
			;
		
			return __this + args;
		}
		
		/**
		 * Determines if we are a constructor.
		 */
		public boolean isConstructor() {
			return true;
		}
	}
	
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
		
		//finally, see if we're the main(String[] args) method.
		this.checkMainMethod();
	}
	
	/**
	 * Checks our name and parameters to see if we're the main method.
	 */
	private void checkMainMethod() {
		//do two really quick checks that will disqualify most methods before we do any deep checks
		if (!this.name.equals("main") || this.sig.size() != 1)
			return;
		
		JavaMethodSignature sig = new JavaMethodSignature();
		sig.add(this.getJavaFile().getImport("java.lang.String").getType(), this);
		
		//if we get here, we might be main -- let's check our signature
		if (this.sig.equals(sig))
			JavaStatic.pkgs.setMainMethod(this);
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
	 * Determines if we are a constructor.
	 */
	public boolean isConstructor() {
		return false;
	}
	
	/**
	 * ==================================================================================================
	 * New Variable Methods(this/chain)
	 */

	/**
	 * A call expression in this method has determined we have chaining.
	 */
	public void hasChaining() {
		this.chaining = true;
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
	 * Gets the arguments for printing.
	 */
	protected String getPrintArguments(boolean withNames, JavaClass cls) {
		String args = this.sig.getCppArguments(withNames);
		
		String __this = "";
		if (!this.isStatic())
			__this = cls.getCppName() + (withNames ? " __this" : "") + (args.length() > 0 ? ", " : "");
		
		return __this + args;
	}
		
	/**
	 * Prints the implementation of this method.
	 */
	public void print(CodeBlock b, JavaClass cls) {
		//we only want to print to our defining class
		//and make sure we're not native -- that would just be a drag!
		if (cls != this.getJavaClass() || this.isNative())
			return;
	
		//in the future this will also print the sig
		b = b.block(this.returnType.getCppName() + " " + this.getJavaClass().getCppName(false, false) + "::" + this.getCppName(false) + "(" + this.getPrintArguments(true, cls) + ")");
		
		//Sets a temporary block to hold all the information from our statements.
		//This also "activates" our method. Since this is guaranteed to only happen once, we can
		//probably remove JavaMethod from activatible visitor.
		CodeBlock block = (CodeBlock)this.dispatch(this.node);
		if (this.chaining)
			//only create a chain variable if we need it
			b.pln("java::lang::Object __chain;");
		//now that we know if we need chaining, we can attach the main block
		b.attach(block);
		b.close();
	}
	
	/**
	 * Prints the method signature to the class definition in the header, if the method is part of
	 * the class.
	 */
	public void printToClassDefinition(CodeBlock b, JavaClass cls) {
		//we only want to print to our defining class
		if (cls != this.getJavaClass())
			return;

		b.pln("static " + this.returnType.getCppName() + " " + this.getCppName(false, false) + "(" + this.getPrintArguments(false, cls) + ");");
	}
	
	/**
	 * Prints the method signature to the vTable.
	 */
	public void printToVTable(CodeBlock b, JavaClass cls) {
		b.pln(this.returnType.getCppName() + " (*" + this.getCppName(false, false) + ")(" + this.getPrintArguments(false, cls) + ");");
	}
	
	/**
	 * Gets the string that goes into the constructor initialization list in the vTable.
	 */
	public String getVTableInitialize(JavaClass cls) {
		String cast = this.getVTableInitializeCast(cls);
		return this.getCppName(false) + "(" + cast + "&" + this.getCppName(true, false) +")";
	}
	
	/**
	 * If the entry in the vTable constructor initilization list needs to be cast, this gives you the proper form of the
	 * cast.
	 */
	protected String getVTableInitializeCast(JavaClass cls) {
		//cast the vTable entry, do we need to cast our entry?
		String cast = "";
		
		if (this.getJavaClass() != cls) {
			cast = this.getSignature().getCppArguments(false);
			cast = "(" + this.getType().getCppName() + "(*)(" + cls.getCppName(true, true) + (cast.length() > 0 ? ", " + cast : "") + "))";
		}
		
		return cast;
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
	 * Gets the method name.
	 */
	public String getCppName() {
		return this.getCppName(true);
	}
	
	/**
	 * Gets the C++ name.
	 */
	public String getCppName(boolean fullName) {
		return this.getCppName(fullName, true);
	}
	
	
	/**
	 * Gets the C++ name.
	 *
	 * @param fullName If true, returns something like "java::lang::Object::getClass", otherwise just "getClass".
	 * @param asPointer Only used if fullName is true; if asPointer is true, returns something like "java::lang::Object::getClass", otherwise just "java::lang::__Object::getClass".
	 */
	public String getCppName(boolean fullName, boolean asPointer) {
		String name = "";
		if (fullName)
			name += this.getJavaClass().getCppName(true, asPointer) + ".";
		
		if (this.mangledName == null)
			JavaStatic.runtime.error("JavaMethod.getCppName(): Method without a mangled name found in \"" + this.getJavaClass().getName() + "\"; method: " + this.name);
		
		return name.replace(".", "::") + this.mangledName;

	}
	
	
	/**
	 * Mangles the name of the method given the names of all the other methods.
	 */
	public void mangleName(HashSet<String> methodNames) {
		//attempt just to use our name
		this.mangledName = this.name;
		
		//if our name is used, we have to be more tricksy
		if (methodNames.contains(mangledName)) {
			String clsName = this.getJavaClass().getName().replace(".", "_");
			String sig = this.sig.getCppMangledArgumentList();
			
			//try to use our name and our signature
			this.mangledName = clsName + "__" + this.name + sig;
			
			//someone is screwing with our names to try to break it...outsmart them :)
			if (methodNames.contains(this.mangledName)) {
				//set up a counter, and just go until we find a name we can use
				int i = 0;
				while (methodNames.contains(this.mangledName)) {
					this.mangledName = clsName + "__" + this.name + "__" + (i++) + sig;
				}
			}
		}
		
		methodNames.add(this.mangledName);
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
	 * Gets this method, fields and expressions o=in this method will need this.
	 */
	public JavaMethod getMyMethod() {
		return this;
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
	 * Visit our formal parameters and add them to our list of fields.
	 * We're using this to take advantage of the scope searching already implemented in fields.
	 */
	public void visitFormalParameter(GNode n) {
		FieldDec.FormalParameters d = new FieldDec.FormalParameters(this, n);
		JavaField field = d.getField();
		this.sig.add(field.getType(), field);
	}
}
