package translator;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashSet;

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Visitor;

//we need every expression type for visiting
import translator.Expressions.*;
import translator.Statements.*;
import translator.Printer.CodeBlock;

/**
 * Handles anything that can have its own scope, from a File to a Block.
 */
public class JavaScope extends Visitor {
	/**
	 * The name of the package.  We include this in all scopes so that we don't have to climb
	 * the tree when printing: we can just directly reference it here.
	 */
	private String pkg;

	/**
	 * The scope that this scope is in (ie the parent scope).
	 */
	private JavaScope scope;
	
	/**
	 * List of all fields in this scope.
	 */
	public LinkedHashMap<String, JavaField> fields = new LinkedHashMap<String, JavaField>();
	
	public int depth = 0;
	
	/**
	 * Do some frikking-sweet calling.
	 */
	public JavaScope(JavaScope scope) {
		this(scope, null);
	}

	/**
	 * Store our parent scope so that we can climb our scope tree.
	 */
	public JavaScope(JavaScope scope, GNode n) {
		this.scope = scope;
		
		//if we have a scope, then save our package name
		if (scope != null) {
			this.pkg = scope.pkg;
			this.depth = this.scope.depth + 1;	
		}

		//do the construct call-back, alright, do the construct call-back, baby.
		this.onInstantiate(n);
	}
	
	/**
	 * A nice, little call-back for when something is constructed.  Just in case classes
	 * need to do something without intercepting the constructor.
	 */
	protected void onInstantiate(GNode n) { }
	
	/**
	 * ==================================================================================================
	 * Scope Methods -- these operate on _this_ scope and the _parent_ scope.
	 */

	/**
	 * Gets the file that this scope is contained in.
	 */
	public JavaFile getJavaFile() {
		if (this instanceof JavaFile)
			return (JavaFile)this;
		
		if (this.scope == null) {
			JavaStatic.runtime.error("Epic fail. Some scope object was not contained in a file.");
			JavaStatic.runtime.exit();
		}
		
		//ask the parent if he is a file
		return this.scope.getJavaFile();
	}
	
	/**
	 * Stupid name conflict with java.lang.Object.getClass()
	 */
	public JavaClass getJavaClass() {
		if (this instanceof JavaClass)
			return (JavaClass)this;
		
		if (this.scope == null) {
			JavaStatic.runtime.error("Epic fail. Some scope object was not contained in a file.");
			JavaStatic.runtime.exit();
		}
		
		//ask the parent if he is a file
		return this.scope.getJavaClass();
	}

	/**
	 * Perhaps I need my scope.
	 */
	public JavaScope getScope() {
		return this.scope;
	}

	/**
	 * Get the method I'm a part of.
	 * This is needed because we're creating new fields in methods (__this/__chain).
	 */
	public JavaMethod getMyMethod() {
		return this.getScope().getMyMethod();
	}
	
	/**
	 * ==================================================================================================
	 * Utility Methods
	 */
	
	/**
	 * Sets the package we are currently in.
	 */
	protected void setPackageName(String pkg) {
		this.pkg = pkg;
	}
	
	/**
	 * Gets the name of the package we are in.
	 */
	public String getPackageName() {
		return this.pkg;
	}

	/**
	 * Add a field to our list of fields.
	 */
	public void addField(JavaField fld) {
		this.fields.put(fld.getName(), fld);
	}
	
	/**
	 * Retrieve a JavaField, if it exists
	 */
	public JavaField getField(String name) {
		if (this.fields.containsKey(name))
			return this.fields.get(name);
		
		if (this.scope == null)
			return null;
		
		return this.scope.getField(name);
	}
	
	/**
	 * Returns an ArrayList of all JavaFields in this scope
	 */
	public ArrayList<JavaField> getAllFields() {	
		return new ArrayList<JavaField>(this.fields.values());
	}
	
	/**
	 * Gets a list of all of the names of the declared fields in this scope.
	 * Take note: it ignores all the fields from any parent scope.
	 */
	public HashSet<String> getDeclaredFields() {
		return new HashSet<String>(this.fields.keySet());
	}
	
	/**
	 * The universal print method. It should be abstract, but it isn't for now
	 * so we can test without having to implement a print method for everything.
	 */
	public void print(CodeBlock b) {
		b.pln(" /*** IMPLEMENT PRINT:  ***/ ");
	}

	/**
	 * ==================================================================================================
	 * Visitor Methods
	 */
	
	/**
	 * Statement Visitors
	 */
	public JavaStatement visitExpressionStatement(GNode n) {
		return new ExpressionStatement(this, n);		
	}
	
	public JavaStatement visitReturnStatement(GNode n) {
		return new ReturnStatement(this, n);
	}

	public JavaStatement visitForStatement(GNode n) {
//		return new ForStatement(this, n);
		return new JavaForStatement(this, n);
	}

	public JavaStatement visitWhileStatement(GNode n) {
		return new WhileStatement(this, n);
	}

	public JavaStatement visitConditionalStatement(GNode n) {
		return new ConditionalStatement(this, n);
	}


	/**
	 * Expressions Visitors
	 */
	public JavaExpression visitCallExpression(GNode n) {
		return new CallExpression(this, n);
	}
	
	public JavaExpression visitPrimaryIdentifier(GNode n) {
		return new Identifier(this, n);
	}
	
	public JavaExpression visitConditionalExpression(GNode n) {
		return new ConditionalExpression(this, n);
	}
	
	public JavaExpression visitRelationalExpression(GNode n) {
		return new ComparativeExp(this, n);
	}

	public JavaExpression visitSelectionExpression(GNode n) {
		return new SelectionExpression(this, n);
	}

	public JavaExpression visitExpression(GNode n) {
		return new ArithmeticExp(this, n);
	}
	
	public JavaExpression visitAdditiveExpression(GNode n) {
		return new ArithmeticExp(this, n);
	}
	
	public JavaExpression visitMultiplicativeExpression(GNode n) {
		return new ArithmeticExp(this, n);
	}
	
	public JavaExpression visitPostfixExpression(GNode n) {
		return new PostfixExp(this, n);
	}
	
	public JavaExpression visitExpressionList(GNode n){
		return new ExpressionList(this,n);
	}
	
	public JavaExpression visitIntegerLiteral(GNode n) {
		return new Literal(this, n, JavaType.getType("int"));
	}
	
	public JavaExpression visitCharacterLiteral(GNode n) {
		return new Literal(this, n, JavaType.getType("char"));
	}
	
	public JavaExpression visitFloatingPointLiteral(GNode n) {
		return new Literal(this, n, JavaType.getType("float"));
	}
	
	public JavaExpression visitStringLiteral(GNode n) {
		return new Literal(this, n, JavaType.getType("java.lang.String")) {
			public String print() {
				return "((" + this.getJavaFile().getImport("java.lang.String").getCppName(true, false) + ")" + this.value + ")";
			}
		};
	}
	
	public JavaExpression visitThisExpression(GNode n) {
		//this is so simple it should just go inline
		return new JavaExpression(this, n) {
			protected void onInstantiate(GNode n) {
				this.setType(this.getJavaClass().getType());
			}
			
			public String print() {
				return "__this";
			}
		};
	}


	/**
	 * Other Visitors
	 */
	 
	/**
	 * Create a FieldDec object, the FieldDec will handle everything else so this is all we need to do.
	 */
	public void visitFieldDeclaration(GNode n) {
		FieldDec f = new FieldDec(this, n);
	}
	
	public CodeBlock visitBlock(GNode n) {
		ArrayList<String> usedFields = new ArrayList<String>();
		CodeBlock block = new CodeBlock(depth);
		
		for (Object o : n) {
			if (o instanceof Node) {
				JavaStatement temp = (JavaStatement)this.dispatch((Node)o);
				if (temp != null)
					temp.print(block);
				else {
					JavaStatic.runtime.warning("JavaScope.visitBlock(): Fix me: I'm printing fields stupidly! (I'm the one printing `args` for main)");
					for (JavaField f : this.fields.values()) { 
						if (!usedFields.contains(f.getName())) {
							f.print(block);
							usedFields.add(f.getName());
						}
					}
				}
			}
		}
		
		return block;
	}
	
	/**
	 * The default visitor method from Visitor.
	 */
	public void visit(Node n) {
		for (Object o : n) {
			if (o instanceof Node)
				this.dispatch((Node)o);
		}
	}
}

