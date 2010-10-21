class test3a {
	public static int m = 1;
	
	public static void printM() {
		System.out.print("The value of m in test3a is currently: ");
		System.out.println(m);
	}
	
	public static void setM() {
		m = 10;
	}
}

public class test3 {
	public static void main(String[] args) {
		test3a.printM();
		test3a.setM();
		test3a.printM();
	}
}
