public class caseCall {
	public void test() {
		caseCall a = new caseCall();
		a.test2().test2();
		test2().test2().test2().test2().test2();
	}
	
	public caseCall test2() {
		return this;
	}
	
	public Object getScope() {
		return new Object();
	}
}

class caseCall2 extends caseCall {
	public String getScope() {
		return "test";
	}
}
