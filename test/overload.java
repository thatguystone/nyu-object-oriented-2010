public class overload {
	public void test() { }
	public void test(String s) { }
	public void test(Object o) { }
	public void test(Class cls) { }
	
	public static void main(String[] args) { 
	
	}
}

class overload_B extends overload {
	public void test() { }
	public void test(String s) { }
	public void test(String[][] s) { }
}
