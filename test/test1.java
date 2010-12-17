class noDependenciesButObject extends java.lang.Object { }

class inheritMe {

	int test;
	char test2;
	static String tmp = "test";
	
	int defaultPkg_needsInherit__test;

	public void printCls(){
		System.out.println("THIS SHOULD NOT BE CALLED");
	}

	public void printInherit() {
		System.out.println();
		System.out.println("You have successfully demonstrated inheritance!");
		System.out.println();
		
		Class cls = this.getClass();
		java.lang.System.out.print("I am of class type (printing from inheritMe): ");
		System.out.println(cls.getName());
		
		Object o = new java.lang.Object();
		
		System.out.println(this.tmp);
	}

}

public class test1 extends inheritMe {
	int test;
	
	public static void main(String[] args) {
		inheritMe inheritor = new needsInherit();
		
		inheritor.printCls();
		inheritor.printInherit();
		
		//uses chaining -- should work when chaining works
		//un-comment-out when chaining ready
		System.out.print("Inline getClass(): " + new needsInherit().getClass().getName());
		
		inheritMe inheritor2 = new inheritMe();
		inheritor2.printInherit();
	}
}

class needsInherit extends inheritMe {
	int test;
	int test2;
	inheritMe tmp;
	java.lang.Object tmp2;

	public void printCls(){
		int iasdf = 1;
		
		switch (1) {
			case 1:
				iasdf = 2;
				inheritMe asdfadsf = new inheritMe();
				break;
			case 2:
				break;
		}
	
		for ( ; ; ) {
			break;
		}
	
		for (int i = 1; i < 10; i++) {
			char cat = 'a';
			int k, j;
			double d = 2.2;
			cat = 'b';
		}
		
		int x;
		
		if (true)
			x = 4 + 7 * 3 - 8;
		
		if (true) {
			x = 4 + 7 * 3 - 8;
		} else if (false) {
			;
		} else if (1 == 2) {
			;
		} else {
			x = 5;
		}
		float ff = 3.3f;
		long ll = 5;
		char cc = 'a';
		byte bb = 1;

		double dd = ff + ll;

		int ii = cc + bb;

		while (x < 8) {
			x++;
		}
		System.out.println("I am a needsInherit object");
		
		System.out.println("My hashCode is: (hidden for testing)");
		this.hashCode();
		
		Class cls = this.getClass();
		System.out.print("I am of class type (printing from needsInherit): ");
		System.out.println(cls.getName());
	}

	needsInherit(){}
		
}

class doubleRainbowExtension extends needsInherit {
	int test;
	int defaultPkg_doubleRainbowExtension__defaultPkg_needsInherit__0_test;
	int defaultPkg_needsInherit__0_test;
	char defaultPkg_doubleRainbowExtension__1_defaultPkg_needsInherit__0_test;
	char defaultPkg_doubleRainbowExtension__0_defaultPkg_needsInherit__0_test;
}
