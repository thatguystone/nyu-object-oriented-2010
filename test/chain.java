public class chain {
	public static void main(String[] args) {
		A a = new A();
		a.aMeth().bMeth().print();
		
		C c = a.aMeth().bMeth();
		c.print();
		
		System.out.println(a.aMeth().bMethInt());
		
		f1.f.j.method();
		
		f1 f = new f1();
		f.f.j.method();
		
		String str = f.f.j.f4Meth().f5Meth().status();
		System.out.println(str);
		
		str = f.f.j.f4Meth().f5Meth().statusNonStatic();
		System.out.println(str);
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

class f1 {
	public static f2 f;
	public f2 m = new f2();
}

class f2 {
	public static f3 j = new f3();
}

class f3 {
	public static void method() {
		System.out.println("f3.method() called");
	}
	
	public f4 f4Meth() {
		return new f4();
	}
}

class f4 {
	public f5 f5Meth() {
		return new f5();
	}
}

class f5 {
	public static String status() {
		return "f5.status() called";
	}
	
	public String statusNonStatic() {
		return "f5.status() called";
	}
}
