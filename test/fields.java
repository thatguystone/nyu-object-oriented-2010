public class fields {
	public static void impliedSelf() {
		System.out.println("Static method called with implied invokation.");
	}
	
	public static void instanceStatic() {
		System.out.println("Static method called on an instance variable.");
	}

	public static void main(String[] args) {
		f1.f.j.method();
		
		f1 f = new f1();
		f.f.j.method();
		
		impliedSelf();
		
		fields flds = new fields();
		flds.instanceStatic();
	}
}

class f1 {
	public static f2 f;
	public f2 m = new f2();
}

class f2 {
	public static f3 j;
}

class f3 {
	public static void method() {
		System.out.println("f3.method() called");
	}
}
