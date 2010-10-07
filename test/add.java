interface test { } 

public class add extends Object implements test {
	private add j;

	private native int test();

	public static int add(int a, int b) {
		return a + b;
	}
		
	public static void main(String args[]) {
		add.add(1, 2);
	}
}
