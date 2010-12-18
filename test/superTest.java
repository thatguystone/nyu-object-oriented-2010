class superTest {

	public static void main(String[] args) {
		B test = new B();
		B test1 = new B(1);
		B test2 = new B(1,1);
	}
}

class A {

	A() {
		System.out.println("This was printed from an implicit 'super' call.");
	}

	A(int x) {
		System.out.println("This was printed from an explicit 'super' call.");
	}

	A(int x, int y) {
		System.out.println("This was printed from an explicit 'super' call with an overloaded constructor.");
	}
}

class B extends A {
	
	B(){
	}
	
	B(int x) {
		super(1);
	}

	B(int x, int y) {
		super(1,1);
	}
}
