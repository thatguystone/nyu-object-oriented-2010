class inheritMe {

	int test;
	char test2;
	
	int defaultPkg_needsInherit__test;

	public void printCls(){
		System.out.println("I am a inheritMe object");
	}

	public void printInherit() {
		System.out.println();
		System.out.println("You have successfully demonstrated inheritance!");
		System.out.println();
		
		Class cls = this.getClass();
		System.out.print("I am of class type (printing from inheritMe): ");
		System.out.println(cls.getName());
	}

}

class needsInherit extends inheritMe {
	int test;
	int test2;
	inheritMe tmp;
	java.lang.Object tmp2;

	public void printCls(){
		System.out.println("I am a needsInherit object");
		
		System.out.print("My hashCode is: ");
		System.out.println(this.hashCode());
		
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

public class test1 extends inheritMe {
	int test;
	
	public static void main(String[] args) {
		needsInherit inheritor = new needsInherit();
		
		inheritor.printCls();
		inheritor.printInherit();
	}
}
