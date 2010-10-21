class test5a {
	public static int m = 1;
	public int n;
	public String ss = "hi";	


	test5a() {
		System.out.println("I'm being printed from test5a constructor!");
	}
	
	public String ret() {
		n = 2;
		this.n = this.n + 2;
		return "I'm a sexy string returned from ret().";
	}
	
	public int intRet() {
		return 1;
	}
	
	public int passArg(int i) {
		return i;
	}
	
	public static void staticSomething() {
		System.out.println("I'm printed from a static method call!");
	}
	
	public static void staticSomethingWithArgs(char a) {
		System.out.print("I'm an argument being printed from a static method: ");
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
	
	public static void setM() {
		m = 10;
	}
	
	public static void printM() {
		System.out.print("This is my static member m: ");
		System.out.println(m);
	}
	
	public void printN() {
		System.out.print("This is my instance member n: ");
		System.out.println(n);
	}
}

class test5b {

	test5b(){}

	public int getVal() {
		return 35463;
	}

}

class test5c extends test5b {
	
	public int x = 3;
	
	test5c() {
	}
	
	public void willIWork() {
		System.out.println(this.getVal());
	}
}

public class test5 {
	public static void main(String[] args) {
		test5a t = new test5a();
		System.out.print("Printing return from test5a.ret(): ");
		System.out.println(t.ret());
		
		System.out.print("Printing return from test5a.intRet(): ");
		System.out.println(t.intRet());
		test5a.setM();
		
		t.printN();
		test5a.printM();
		
		System.out.print("Printing a value returned from a call to test5a.passArg(): ");
		System.out.println(t.passArg(1));
	
		test5 h = new test5();
		int i;
		int x = 1;
		for (i = 0; i < 10; i++) {	
			if (i > 4) {
				x = x*2;
				System.out.print("Printing a value from inside a for loop: ");
				System.out.print(i);
				System.out.print(" -- ");
				System.out.println(x);
			}
		}
		
		test5b d = new test5b();
		
		test5c e = new test5c();
		
		System.out.print("Testing calls to functions in parent (35463 prints if it works): ");
		e.willIWork();
		
		boolean hh = true;
		if(hh){
			System.out.println("I'm being printed from within an IF statement!");
		}
		
		i = 0;
		while(i < 10){
			System.out.print("Printing from a while loop (i=): ");
			System.out.println(i);
			i++;
		}
		
		System.out.print("Print some x as set in the above for loop: ");
		System.out.println(x);
		h.hello();
		
		test5a.staticSomething();
		test5a.staticSomethingWithArgs('a');
		
		System.out.print("Printing return from a static function with 1 argument: ");
		System.out.println(test5a.staticSomethingReturnWithArgs('b'));
		
		System.out.print("Printing return from a static function with 2 arguments: ");
		System.out.println(test5a.staticSomethingReturnWith2Args(1, 2));
		
		System.out.print("Printing return from a static function with 5 arguments: ");
		System.out.println(test5a.staticSomethingReturnWith5Args(1, 2, 4, 5, 6));
	}

	public void hello() {
		System.out.println("I'm saying hello to you!");
	}
}
