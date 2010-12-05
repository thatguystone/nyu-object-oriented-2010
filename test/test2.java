public class test2 {
	public String tmp;	
	
	public void updateTmp() {
		tmp = "I am a property of test2 set in updateTmp()";
	}
	
	public static void main(String[] args) {
		test2 t = new test2();
		int x = 1;
		long l = 3;
		char c = 'a';
		double d = 3.3;
		float f = (float)1.1;
		x = 1 + 3 * 4;
		t.updateTmp();
		System.out.println(t.tmp);
	}
}
