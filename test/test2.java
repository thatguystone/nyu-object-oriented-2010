public class test2 {
	public String tmp;	
	
	public void updateTmp() {
		tmp = "I am a property of test2 set in updateTmp()";
	}
	
	public static void main(String[] args) {
		test2 t = new test2();
		t.updateTmp();
		System.out.println(t.tmp);
	}
}
