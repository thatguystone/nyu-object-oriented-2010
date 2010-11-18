package translator.Statements;

import translator.JavaScope;

import java.util.ArrayList;
import xtc.tree.Node;
import xtc.tree.GNode;
/*
<<<<<<< HEAD
class JavaForStatement extends JavaStatement{
=======
class JavaForStatement  extends JavaScope{
	//JavaBasicForControl ctrl;
>>>>>>> c39b4fded863607882005a55f95f1ad059ee9360
	JavaForStatement(JavaScope scope, GNode n){
		super(scope, n);
	}
<<<<<<< HEAD
	public JavaBasicForControl visitBasicForControl(GNode n){
		return new JavaBasicForControl(this,n);
	}
	public CodeBlock printMe(CodeBlock block){
		return new CodeBlock(block,"for (" + header.printMe + ")",true).close();  /** THEN WHERE IS THE BEEF?? **/
		/** i.e. what to do with the printing of subscope(i) **/
/*
=======
	public void visitBasicForControl(GNode n){
		//ctrl=new JavaBasicForControl(this,n);
>>>>>>> c39b4fded863607882005a55f95f1ad059ee9360
	}
}
*/
