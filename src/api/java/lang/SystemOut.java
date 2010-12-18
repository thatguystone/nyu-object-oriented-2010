package java.lang;

public class SystemOut {
	public void print() { }
	public native void print(String s);
	public native void print(int i);
	public native void print(char c);
	public native void println();
	public native void println(String s);
	public native void println(int i);
	public native void println(char c);
}
