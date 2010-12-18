package java.lang;

public class Object {
	public final Class getClass() {
		return VMManager.getClassNative(this);
	}
	
	public int hashCode() {
		return VMManager.getHashCodeNative(this);
	}
	
	public boolean equals(Object o) {
		return this == o;
	}
	
	public native String toString();
}
