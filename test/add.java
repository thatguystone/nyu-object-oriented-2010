import test2.add2;

interface test1 { } 

class test {
	protected String som() {
		return "test";
	}
	
	public String toString() {
		return "Hello";
	}
}

public class add extends test implements test1 {
	private static String something;
	private add j;

	private native int test();

	public static int add(int a, int b) {
		return a + b;
	}
	
	public String som() {
		return "add";
	} 
		
	public static void main(String args[]) {
		add.add(1, 2);
	}
}
