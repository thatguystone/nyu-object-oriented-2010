package java.lang;

public class Class {
	private String name;
	private Class parent;
	private boolean primitive;

	public Class(String name, Class parent, boolean prim) {
		this.name = name;
		this.parent = parent;
		this.primitive = prim;
	}
	
	public String getName() {
		return this.name;
	}
	
	public native boolean isInstance(Object o);
	public native Class getSuperclass();
}
