package translator.Statements;

import translator.JavaScope;
import translator.Printer.CodeBlock;

import java.util.ArrayList;

import xtc.tree.GNode;

public class JavaStatement extends JavaScope {

	public JavaStatement(JavaScope scope, GNode n){
		super(scope, n);
	}
}

