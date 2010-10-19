package java.lang;

public class Object {
	public final Class getClass() {
		//return VMManager.getClass(this);
		return null;
	}
	
	public int hashCode() {
		//return VMManager.getHashCode(this);
		return 1;
	}
	
	public boolean equals(Object o) {
		return this == o;
	}
	
	public String toString() {
		return getClass().getName() + '@' + hashCode();
	}
}
