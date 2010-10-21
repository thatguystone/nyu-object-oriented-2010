public class Array {
	/**
	* Since we are using templates for C++ arrays, using Java Generics
	* Not quite sure about this
	*/

	/**
	* Check if the index is out of bounds
	*/
	public native void checkIndex(int index);
	
	/**
	* Get the value of an element at an index
	*/
	public native Object get(int index);
	
	/**
	* Set the value of an element at an index
	*/
	public native void set(int index, Object value);
	
	/**
	* Checks if types are compatible with Array type (objects only)
	*/
	public native void checkType(Object object);
}
