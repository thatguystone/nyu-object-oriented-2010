package translator.Statements;

import java.util.ArrayList;

import translator.JavaScope;
import translator.JavaStatic;
import translator.JavaField;
import translator.FieldDec;
import translator.Expressions.JavaExpression;
import translator.Printer.CodeBlock;

import xtc.tree.GNode;

public class ForStatement extends JavaStatement {
	public ForStatement (JavaScope scope, GNode n) {
		super(scope, n);
	}

	public void print(CodeBlock b) {
		GNode control = (GNode)this.node.get(0);
		
		//get all the initilization fields
		new FieldDec(this, GNode.create("FieldDec", control.get(0), control.get(1), control.get(2)));
		ArrayList<JavaField> fields = this.getAllFields();
		
		String line = "for (";
		
		//------------------------------------------------------------------------------------------------
		//handle the initilization printing
		
		if (fields.size() == 0) {
			//no initilization
			line += " ;";
		} else {
			line += fields.get(0).getType().getCppName() + " ";
			
			//emit all the fields, separated by commas, to the printing
			for (JavaField f : fields)
				line += f.getCppField(false) + ", ";
			
			//remove the last comma and replace it with a semicolon
			line = line.substring(0, line.length() - 2) + ";";
		}
		
		//------------------------------------------------------------------------------------------------
		//handle the test printing
		
		if (control.get(3) == null) {
			//no test
			line += " ;";
		} else {
			JavaExpression e = (JavaExpression)this.dispatch((GNode)control.get(3));
			
			//if we don't have an expression returned, that's just an error
			if (e == null) {
				JavaStatic.runtime.error("Statements.ForStatement: No expression type found for for-statement test.");
				line += " NO TEST EXPRESSION FOUND;";
			} else {
				line += " " + e.print() + ";";
			}
		}
		
		//------------------------------------------------------------------------------------------------
		//handle update printing
		
		if (control.get(4) == null) {
			//no update
			line += " ";
		} else {
			JavaExpression e = (JavaExpression)this.dispatch((GNode)control.get(4));
			
			//if we don't have an expression returned, that's just an error
			if (e == null) {
				JavaStatic.runtime.error("Statements.ForStatement: No expression type found for for-statement update.");
				line += " NO UPDATE EXPRESSION FOUND";
			} else {
				line += " " + e.print();
			}
		}
		
		//------------------------------------------------------------------------------------------------
		//and do the final printing
		
		b
			.block(line + ")")
			.attach((CodeBlock)this.dispatch((GNode)this.node.get(1)))
			.close()
		;
	}
}
