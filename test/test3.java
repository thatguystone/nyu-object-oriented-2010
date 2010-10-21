class test3a {
	public static int m = 1;
	
	public static void printM() {
		System.out.print("The value of m in test3a is currently: ");
		System.out.println(m);
	}
	
	public static void setM() {
		m = 10;
	}
	
	public void setMTest() {
		m = 20;
		this.test();
	}
	
	private void test() {
		m = 30;
	}
}

public class test3 {
	public static void main(String[] args) {
		test3a.printM();
		test3a.setM();
		test3a.printM();
		
		test3a t = new test3a();
		t.setMTest();
		
		test3a.printM();
	}
}
