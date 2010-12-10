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
	
	/**
	 * Handles the contrived nature of fields in the Java for-statement.
	 *
	 * @param control The GNode that represents the control area of the for-statement.
	 */
	private String handleFields(GNode control) {
		String ret = "";
	
		//if we just have some expressions hanging around, no field declarations
		if (control.get(0) == null && control.get(1) == null) {
			//if we actually have anything declared in the field area
			if (control.get(2) != null)
				ret += ((JavaExpression)this.dispatch((GNode)control.get(2))).print();
		} else {
			//get all the initilization fields
			new FieldDec.ScopeParameters(this, GNode.create("FieldDec", control.get(0), control.get(1), control.get(2)));
			ArrayList<JavaField> fields = this.getAllFields();
			
			if (fields.size() > 0) {
				ret += fields.get(0).getType().getCppName() + " ";
			
				//emit all the fields, separated by commas, to the printing
				for (JavaField f : fields)
					ret += f.getCppField(false) + ", ";
			
				//remove the last comma and replace it with a semicolon
				ret = ret.substring(0, ret.length() - 2);
			}
		}
		
		ret += ";";
		
		return ret;
	}
	
	public void print(CodeBlock b) {
		GNode control = (GNode)this.node.get(0);
		
		String line = "for (";
		
		//------------------------------------------------------------------------------------------------
		//handle the initilization printing
		
		line += this.handleFields(control);
		
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
