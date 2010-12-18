class superTest {

	public static void main(String[] args) {
		B test = new B();
		B test1 = new B(1);
		B test2 = new B(1,1);
		test.print();
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

	void print() {
		System.out.println("printing from super class");
	}

	void print(int x) {
		System.out.println("printing from overloaded super class method");
	}
}

class B extends A {
	
	B(){
		System.out.println("2");
	}
	
	B(int x) {
		super(1);
	}

	B(int x, int y) {
		super(1,1);
	}

	void print() {
		super.print();
		super.print(1);
	}
}
