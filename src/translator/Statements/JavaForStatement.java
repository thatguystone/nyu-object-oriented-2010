package translator;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import xtc.tree.Node;
import xtc.tree.GNode;

class JavaForStatement extends JavaScope{
	private String type;  /** type of the field **/
	JavaForStatement(JavaScope scope, GNode n){
		super(scope, n);
		this.dispatch(n);
	}
	protected void onInstantiate(GNode n){
		/** may not need to do anything here **/
	}
	public void visitBasicForControl(GNode n){
		/** first attempt to "normalize" for control **/
		/** if there is any initialization, then initialization(s) can actually be treated the same as field dec **/
		if(n.get(0)!=null){
			new JavaForInit(this,n);
		}else{
			/** will fill in other details later **/
		}
	}
}
