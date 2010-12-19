class superTest {

	public static void main(String[] args) {
		A test = new B();
		B testt = (B)test;
		if (test instanceof B)
			System.out.println("instanceof");
		B test1 = new B();
		A testtt = (true?test1:test);
		System.out.println(test);
		B test2 = new B(1,1);
		int x = true?1:2;
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
		print();
		super.print();
		super.print(1);
	}
	
	B(int x) {
		super(1);
	}

	B(int x, int y) {
		super(1,1);
	}

	void print() {
		System.out.println("printing from class");
	}
}
