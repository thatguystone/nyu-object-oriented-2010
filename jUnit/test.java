import junit.framework.TestCase;

public class test extends TestCase {
	public void testGood() {
		assertEquals(0, 0);
	}
	
	public void testBad() {
		assertEquals(0, 0);
		assertEquals(0, 1);
	}
}
