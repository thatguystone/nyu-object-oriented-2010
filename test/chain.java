public class chain {
	public static void main(String[] args) {
		A a = new A();
		a.aMeth().bMeth().print();
		
		C c = a.aMeth().bMeth();
		c.print();
		
		System.out.println(a.aMeth().bMethInt());
	}
}

class A {
	public B aMeth() {
		return new B();
	}
}

class B {
	public C bMeth() {
		return new C();
	}
	
	public int bMethInt() {
		return 1;
	}
}

class C {
	public void print() {
		System.out.println("C.print()");
	}
}
