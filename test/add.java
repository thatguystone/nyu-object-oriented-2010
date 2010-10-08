import test2.add2;

interface test1 { } 

public class add extends Object implements test1 {
	private add j;

	private native int test();

	public static int add(int a, int b) {
		return a + b;
	}
		
	public static void main(String args[]) {
		add.add(1, 2);
	}
}
