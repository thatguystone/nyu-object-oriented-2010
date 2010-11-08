public class Exp {
	public static void main(String[] args) {
		String tmp = ("foo" + 4).intern();
		System.out.println("foo".length());
		System.out.println("foo".getClass().getName());
		System.out.println(tmp);
	}
}

class __test { } 
