class inheritMe {

	public void printCls(){
		System.out.println("I am a inheritMe object");
	}

	public void printInherit() {
	
	System.out.println();
	System.out.println("You have successfully demonstrated inheritance!");
	System.out.println();
	
	}

}


class needsInherit extends inheritMe {

	public void printCls(){
		System.out.println("I am a needsInherit object");
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
