class test {
	public static int m = 1;
	public int n;
	public String ss = "hi";	


	test() {
		System.out.println("Hello from Mr. Test");
	}
	
	public String ret() {
		n = 2;
		return "Hey sexy.";
	}
	
	public int intRet() {
		return 1;
	}
	
	public int passArg(int i) {
		return i;
	}
	
	public static void staticSomething() {
		System.out.println("Call from static!");
	}
	
	public static void staticSomethingWithArgs(char a) {
		System.out.println(a);
	}
	
	public static char staticSomethingReturnWithArgs(char a) {
		return a;
	}
	
	public static int staticSomethingReturnWith2Args(int a, int b) {
		return a + b;
	}
	
	public static int staticSomethingReturnWith5Args(int a, int b, int c, int d, int e) {
		return (a * b) + c + d + e;
	}
}

public class helloWorld {
	public static void main() {
		helloWorld h = new helloWorld();
		int i;
		int x = 1;
		for (i = 0; i < 10; i++) {	
			if (i > 4) {
				x = x*2;
				System.out.print(i);
				System.out.print(" -- ");
				System.out.println(x);
			}
		}
		boolean hh = true;
		if(hh){
			System.out.println("IF STATEMENT!");
		}
		i = 0;
		while(i < 10){
			System.out.print("Number : ");
			System.out.println(i);
			i++;
		}
		System.out.println(x);
		h.hello();
		
		test.staticSomething();
		test.staticSomethingWithArgs('a');
		System.out.println(test.staticSomethingReturnWithArgs('b'));
		System.out.println(test.staticSomethingReturnWith2Args(1, 2));
		System.out.println(test.staticSomethingReturnWith5Args(1, 2, 4, 5, 6));
	}

	public void hello() {
		Class cls = getClass();
		System.out.println("hello world");
		
		test t = new test();
		//t.test();
		System.out.println(t.ret());
		System.out.println(t.intRet());
		
		System.out.println(t.passArg(1));
	}
}
