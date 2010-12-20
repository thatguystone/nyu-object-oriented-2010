package java.util;

public class ArrayList {
	
	private Object[] array;
	private boolean isFull;
	private int arraySize;
	
	/**
	 * JavaArrayList constructor with no arguments
	 * Default size is 10
	 */
	public ArrayList() {
		this.array = new Object[10];
		this.isFull = false;
		this.arraySize = 0;
	}
	
	/**
	 * JavaArrayList constructor with length initializer
	 */
	public ArrayList(int length) {
		this.array = new Object[length];
		this.isFull = false;
		this.arraySize = 0;
	}
	
	/**
	 * Inserts the specified element at the specified position in this list.
	 */
	public void add(int index, Object element) {
		
		/**
		 * If an object is added out of bounds make a new JavaArrayList
		 * with enough space so that the object can be added
		 *
		 * NOTE that the Java API's ArrayList class does not actually do
		 * this, but rather throws an IndexOutOfBoundsException.
		 *
		 * Eh who cares
		 
		 
		 * find out how long the new JavaArrayList needs to be in order to
		 * accomodate the index
		 */
		if (index >= this.array.length) {
		
			// create a new array big enough for the index
			Object[] newArray = new Object[index + 1];

			// copy all elements from old array into new array
			for (int i = 0; i < this.array.length; i++) {
				newArray[i] = this.array[i];
			}
			
			// now add the object at the index
			newArray[index] = element;
			
			// and set the updated array
			this.array = newArray;
		}
		else {
			this.array[index] = element;
		}
		this.arraySize++;
	}
	
	/**
	 * Appends the specified element to the end of this list.
	 */
	public void add(Object element) {
		// check if the array is full
		if (this.isFull) {
			// create a new array big enough for the index
			Object[] newArray = new Object[this.array.length + 1];
			
			// copy all elements from old array into new array
			for (int i = 0; i < this.array.length; i++) {
				newArray[i] = this.array[i];
			}
			
			// and set the updated array
			this.array = newArray;
		}
		else {
			for (int i = 0; i < this.array.length; i++) {
				if (this.array[i] == null) {
					this.array[i] = element;
					if (i == this.array.length - 1) {
						this.isFull = true;
					}
					break;
				}
			
			}
		}
		this.arraySize++;
	}	

	/**
	 * Removes all of the elements from this list.
	 */
	public void clear() {
		for (int i = 0; i < this.array.length; i++) {
			this.array[i] = null;
		}
		this.arraySize = 0;
	}
	
	/**
	 * Returns a shallow copy of the JavaArrayList instance.
	 */
	public Object clone() {
		return this;
	}	
	
	/**
	 * Returns true if this list contains the specified element.
	 */
	public boolean contains(Object elem) {
		for (int i = 0; i < this.array.length; i++) {
			if (this.array[i].equals(elem)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the element at the specified position in this list.
	 */
	public Object get(int index) {
		return this.array[index];
	}
	
	/**
	 * Searches for the first occurence of the given argument, 
	 * testing for equality using the equals method.
	 */
	public int indexOf(Object elem) {
		for (int i = 0; i < this.array.length; i++) {
			if (this.array[i].equals(elem)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Tests if this list has no elements.
	 */
	 public boolean isEmpty() {
	 	if (this.arraySize == 0) {
	 		return true;
	 	}
	 	else {
	 		return false;
	 	}
	 }
	 
	/**
	 * Returns the index of the last occurrence of the specified object
	 * in this list.
	 */
	public int lastIndexOf(Object elem) {
		for (int i = this.array.length - 1; i >= 0; i--) {
			if (this.array[i].equals(elem)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Removes the element at the specified position in this list.
	 */
	public Object remove(int index) {
		Object elem = this.array[index];
		for (int i = index + 1; i < this.array.length; i++) {
			this.array[i - 1] = this.array[i];
		}
		if (this.array[this.array.length - 1] != null) {
			this.array[this.array.length - 1] = null;
		}
		this.arraySize--;
		return elem;
	}
	
	/**
	 * Removes from this List all of the elements whose index is between
	 * fromIndex, inclusive and toIndex, exclusive.
	 */
	protected void removeRange(int fromIndex, int toIndex) {
		for (int i = fromIndex; i <= toIndex; i++) {
			this.remove(fromIndex);
		}
	}
	
	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 */
	 public Object set(int index, Object element) {
	 	Object previous = this.array[index];
	 	this.array[index] = element;
	 	return previous;
	 }
 
 	/**
	 * Returns the number of elements in this list.
	 */
	 public int size() {
	 	return this.arraySize;
	 }
	 
	/**
	 * Returns an array containing all of the elements in this list in the correct order.
	 */
	 public Object[] toArray() {
	 	return this.array;
	 }
 
	/**
	 * Prints the JavaArrayList contents
	 * Used for testing purposes only
	 *
	 public void print() {
	 	for (Object element : this.array) {
	 		System.out.println(element);
	 	}
	 	System.out.println();
	 }
	/* 
	public static void main(String args[]) {
		JavaArrayList array = new JavaArrayList(3);
		System.out.println("Add integers to the JavaArrayList");
		array.add(0, new Integer(3));
		array.add(new Integer(2));
		array.add(new Integer(2));
		array.add(new Integer(1));		
		array.print();
		
		/*
		System.out.println("Copy the JavaArrayList");
		JavaArrayList copy = (JavaArrayList)array.clone();
		//copy.clear();
		copy.print();
		
		System.out.println("Add a new integer to the array");
		array.add(new Integer(0));
		array.print();
		*/
			
		/*		
		Integer i = new Integer('a');
		System.out.println(array.contains(i));
		*/
		//System.out.println(array.get(3));
		//System.out.println(array.indexOf(new Integer(4)));
		/*
		array.clear();
		System.out.println(array.isEmpty());
		
		/*
		System.out.println(array.indexOf(new Integer(2)));	
		System.out.println(array.lastIndexOf(new Integer(2)));
		*/
		/*
		System.out.println("We just removed: " + array.remove(3));
		array.print();
		System.out.println("We just removed: " + array.remove(0));
		array.print();
		*/
		/*
		System.out.println("Removing elements 1 - 2");
		array.removeRange(1,2);
		array.print();
		*/
		/*
		System.out.println("Previous object = " + array.set(0, new Integer(9)));
		array.print();
		*/
		/*
		System.out.println("Size is " + array.size());
		System.out.println(array.isEmpty());
		array.removeRange(0, array.size() - 1);
		array.print();
		System.out.println("Size is " + array.size());
		System.out.println(array.isEmpty());
		*/
		/*
		Object[] myArray = array.toArray();
		System.out.println(myArray.length);
		
	}
	*/
}
