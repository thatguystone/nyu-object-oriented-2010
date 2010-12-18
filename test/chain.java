public class chain {
	public static void main(String[] args) {
		chain_A a = new chain_A();
		a.aMeth().bMeth().print();
		
		chain_C c = a.aMeth().bMeth();
		c.print();
		
		System.out.println(a.aMeth().bMethInt());
		
		chain_f1.f.j.method();
		
		chain_f1 f = new chain_f1();
		f.f.j.method();
		
		//f2.j = new f3();
		
		String str = f.f.j.f4Meth().f5Meth().status();
		System.out.println(str);
		
		str = f.f.j.f4Meth().f5Meth().statusNonStatic();
		System.out.println(str);
	}
}

class chain_A {
	public chain_B aMeth() {
		return new chain_B();
	}
}

class chain_B {
	public chain_C bMeth() {
		return new chain_C();
	}
	
	public int bMethInt() {
		return 1;
	}
}

class chain_C {
	public void print() {
		System.out.println("chain_C.print()");
	}
}

class chain_f1 {
	public static chain_f2 f;
	public chain_f2 m = new chain_f2();
}

class chain_f2 {
	public static chain_f3 j = new chain_f3();
	//public static chain_f3 j;
}

class chain_f3 {
	public static void method() {
		System.out.println("chain_f3.method() called");
	}
	
	public chain_f4 f4Meth() {
		return new chain_f4();
	}
}

class chain_f4 {
	public chain_f5 f5Meth() {
		return new chain_f5();
	}
}

class chain_f5 {
	public static String status() {
		return "chain_f5.status() called";
	}
	
	public String statusNonStatic() {
		return "chain_f5.status() called";
	}
}
