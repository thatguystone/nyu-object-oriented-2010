class inheritMe {

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

	public void printCls(){
		System.out.println("I am a needsInherit object");
		
		Class cls = this.getClass();
		System.out.print("I am of class type (printing from needsInherit): ");
		System.out.println(cls.getName());
	}

	needsInherit(){}
		
}

public class test1 extends inheritMe {

	
	public static void main(String[] args) {
		needsInherit inheritor = new needsInherit();
		
		inheritor.printCls();
		inheritor.printInherit();
	}
}
