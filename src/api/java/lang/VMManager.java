package java.lang;

public class VMManager {
	public static native Class getClassNative(Object o);
	public static native int getHashCodeNative(Object o);
}
