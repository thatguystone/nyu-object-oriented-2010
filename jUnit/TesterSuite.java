import junit.framework.Test;
import junit.framework.TestSuite;

public class TesterSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(test.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
