class test {
	public void test() {
		System.out.println("Hello from Mr. Test");
	}
	
	public String ret() {
		return "Hey sexy.";
	}
	
	public int intRet() {
		return 1;
	}
	
	public int passArg(int i) {
		return i;
	}
}

public class helloWorld {
	public static void main() {
		helloWorld h = new helloWorld();
		h.hello();
	}

	public void hello() {
		Class cls = getClass();
		System.out.println("hello world");
		
		test t = new test();
		t.test();
		System.out.println(t.ret());
		System.out.println(t.intRet());
		
		System.out.println(t.passArg(1));
	}
}
