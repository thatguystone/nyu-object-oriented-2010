public class cast {
	public static void main(String[] args) {
		cast_test t = new cast_test2();
		
		try {
			cast_test2 t2 = (cast_test2)new cast_test();
		} catch (Exception e) {
			System.out.println("Cast failed.");
		} finally {
			System.out.println("That was fun.");
		}
	}
}

class cast_test {

}

class cast_test2 extends cast_test {

}
