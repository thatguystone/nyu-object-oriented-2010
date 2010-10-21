package java.lang;

public class Class {
	private String name;
	private Class parent;
	private Class comp;
	private boolean primitive;
	

	Class(String name, Class parent) {
		this(name, parent, null, false);
	}

	Class(String name, Class parent, Class comp, boolean prim) {
		this.name = name;
		this.parent = parent;
		this.comp = comp;
		this.primitive = prim;
	}
	
	public String getName() {
		return this.name;
	}
}
