import test2.*;

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

	public static int add(int a, int b) {
		int i = 1;
		Object o = new Object();
		return a + b;
	}
	
	public String som() {
		return "add";
	} 
		
	public static void main(String args[]) {
		add.add(1, 2);
	}
}
