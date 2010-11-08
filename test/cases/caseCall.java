public class caseCall {
	public void test() {
		caseCall a = new caseCall();
		a.test2("").test2("a");
	}
	
	public caseCall test2(String a) {
		return this;
	}
}

class caseCall2 extends caseCall {
	public String getScope() {
		return "test";
	}
}
