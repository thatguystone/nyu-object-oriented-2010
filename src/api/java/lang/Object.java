package java.lang;

public class Object {
	public final Class getClass() {
		return VMManager.getClass(this);
	}
	
	public int hashCode() {
		return VMManager.getHashCode(this);
	}
	
	public boolean equals(Object o) {
		return this == o;
	}
	
	public String toString() {
		return getClass().getName() + '@' + hashCode();
	}
}
