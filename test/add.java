public class add {
	private add j;

	private native int test();

	public static void main(String[] args) {
		int i = 3 + 7;
	}
	
	public int add(int a, int b) {
		VMManager.getClass(this);
		return a + b;
	}
}
