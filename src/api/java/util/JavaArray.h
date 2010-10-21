/**
 * Array.h contains the declarations for generic arrays using C++ templates.
 * Creates the Array and Array_VT interface 
 */
 
#pragma once

#include <stdarg.h>
#include <vector>
#include "java_lang.h"
#include <iostream>

namespace java {
	namespace util {

		using namespace java::lang;
		using namespace std;
		/**
		 * For simplicity, we use C++ inheritance for exception types
		 * and throw them by value (see below).  In other words, the
		 * translator does not support user defined exceptions and simply
		 * uses a few built-in classes.
		 */
		 
		class Throwable {
	    	};
		
		class Exception : public Throwable {
		};
	
		class RuntimeException : public Exception {
		};
		
		class NullPointerException : public RuntimeException {
		};
	
		class ArrayStoreException : public RuntimeException {
		};
	
		class ClassCastException : public RuntimeException {
		};
	
		class IndexOutOfBoundsException : public RuntimeException {
		};
	
		class ArrayIndexOutOfBoundsException : public IndexOutOfBoundsException {
		};

		/**
		 * Forward declarations of data layout and vtables.
		 */
		template <typename T>	
		struct __JavaArray;

		template <typename T>
		struct __JavaArray_VT;
			
	
		/**
		 * Definition of convenient type names.
	    	 */
		//typedef __JavaArray<T>* JavaArray;
	    	//typedef __JavaArray_VT<T>* JavaArray_VT;
    	
	    	/**
	    	 * The data layout for arrays.
		 */
		template <typename T>
		struct __JavaArray {
			__JavaArray_VT<T>* __vptr;		// pointer to the vtable
			vector<int32_t> lengths;	// length(s) of the array
			T* __data;			// pointer to the array holding our data
			int32_t dimensions;		// number of dimensions in array
			int32_t unary_length;	// master length. (multidimensional arrays only)
		
			/**
			 * Constructor for Array
			 */
			public: __JavaArray(const int32_t dimensions , ...) : __vptr(&__vtable), dimensions(dimensions) {
				
				// used for variable length arguments
				va_list args; 
				va_start(args, dimensions);
				
				// get the lengths, from the argument list
				vector<int32_t>::iterator it;
				for (int32_t i = 0; i < dimensions; i++) {
					lengths.push_back(va_arg(args, int32_t));
				}

				// we need to make sure that the number of dimensions are correct
				// cout << "dimensions = " << dimensions << "lengths = " << lengths.size() << endl;
				if (dimensions != lengths.size()) {
	          			throw ArrayIndexOutOfBoundsException();
	        		}	
				
				// set the unary length
				for (it=lengths.begin(); it < lengths.end(); it++) {
					unary_length *= *it;
				}

				// create our array
				__data = new T[unary_length];
				
				// "allocate" the array (all zeros)
				std::memset(__data, 0, unary_length * sizeof(T));	
				
				// print the lengths vector (testing purposes only)
				/*
				it = lengths.begin();
				for (it=lengths.begin(); it < lengths.end(); it++) {
					std::cout << *it << " " << std::endl;
				}*/
			}
		
			/**
			 * Returns Class object.  Implemented in Array.cc
			 */
	      		static Class __class();
		
			/**
			 * private vtable.  1 per class
			 */
	    		private:
	      			static __JavaArray_VT<T> __vtable;
	    	};

	    	/**
	    	 * The vtable layout for arrays.
	    	 */
	    	template <typename T>
	    	struct __JavaArray_VT {
	      		typedef __JavaArray<T>* Array;
	      		Class __isa;
	      		int32_t (*hashCode)(Array);
	      		bool (*equals)(Array, Object);
	      		Class (*getClass)(Array);
	      		String (*toString)(Array);
  
	      		/**
	      		 * Constructor for Array_VT
	      		 */
	      		__JavaArray_VT()
	      		: __isa(__Object::__class()),
	        	hashCode((int32_t(*)(Array))&__Object::hashCode),
	        	equals((bool(*)(Array,Object))&__Object::equals),
	        	getClass((Class(*)(Array))&__Object::getClass),
	        	toString((String(*)(Array))&__Object::toString) {
	      		}
	    	};

		/**
    		 * The header file declares each template (see above) just as for
    		 * regular C++ classes.  However, since the compiler needs to know
    		 * how to instantiate each template, the header file also defines
    		 * each template.
    		 */

    		/**
    		 * The vtable for arrays.  Note that this definition uses the
    		 * the default no-arg constructor.
    		 */
    		template <typename T>
    		__JavaArray_VT<T> __JavaArray<T>::__vtable;

		/**
	    	 * Template function to check against null values.
	    	 */
	    	template<typename T>
	    	void __checkNotNull(T o) {
	    		if (0 == o) throw NullPointerException();
	    	}
		
		/**
		 * Template function to set an element in an array
		 */
		template <typename T>
		static void set(__JavaArray<T>* __this, T value, ...) {
			// keeps track of our iterations
			int32_t turn = 0;
			int32_t element = 0;
	
			// used for variable length arguments
			va_list args; 
			va_start(args, value);
			
			// get a copy of the parameters
			vector<int32_t> parameters;
			vector<int32_t>::iterator it;
			for (it = __this->lengths.begin(); it < __this->lengths.end(); it++) {
				parameters.push_back(va_arg(args, int32_t));
			}
			
			// check out of bounds
			checkIndex(__this, parameters);
	
			// variables used for calculating dimension conversion
			int32_t offset = __this->lengths.size() - 1;	
			int32_t end = parameters.size() - 1;
			int32_t converted = __this->lengths.at(offset);
			int32_t dim;
			
			// using offset conversion, set the array at an index(indices)
			while(turn < offset) {
				dim = parameters.at(end);
				if (turn == 0)
					element += (dim + 1) * 1;
				else
					element += (dim + 1) * converted;
				
				// update variables
				offset--;
				converted *= __this->lengths.at(offset);
				turn++;
				end--;
			}
			__this->__data[element] = value;
		}

		/**
		 * Template function to get an element in an array
		 */
		template <typename T>
	      	static T get(__JavaArray<T>* __this, int32_t index) {
			__JavaArray<T>::checkIndex(__this, index);        
			return __this->__data[index];
	      	}

		/**
		 * Template function to get an element in an array
		 */
		template <typename T>
		static T get(__JavaArray<T>* __this, ...) {
			
			// keeps track of our iterations
			int32_t turn = 0;
			int32_t element = 0;
	
			// used for variable length arguments
			va_list args; 
			va_start(args, __this);
			
			// get a copy of the parameters
			vector<int32_t> parameters;
			vector<int32_t>::iterator it;
			for (it = __this->lengths.begin(); it < __this->lengths.end(); it++) {
				parameters.push_back(va_arg(args, int32_t));
			}
			
			// check out of bounds
			checkIndex(__this, parameters);
	
			// variables used for calculating dimension conversion
			int32_t offset = __this->lengths.size() - 1;	
			int32_t end = parameters.size() - 1;
			int32_t converted = __this->lengths.at(offset);
			int32_t dim;
			
			// using offset conversion, set the array at an index(indices)
			while(turn < offset) {
				dim = parameters.at(end);
				if (turn == 0)
					element += (dim + 1) * 1;
				else
					element += (dim + 1) * converted;
				
				// update variables
				offset--;
				converted *= __this->lengths.at(offset);
				turn++;
				end--;
			}
			return __this->__data[element];
		}
	
		/**
		 * Template function to check if an index is out of bounds
		 */
		template <typename T>
	      	static void checkIndex(__JavaArray<T>* __this, vector<int32_t> parameters) {
	        	
      			int32_t length;
			int32_t i = 0;
	      			
	        	// check the index(indices) against our vector of lengths
	        	vector<int32_t>::iterator it;
			for (it= __this->lengths.begin(); it < __this->lengths.end(); it++){
				length = parameters.at(i);
				if (0 > length || length >= *it) {
	          			throw ArrayIndexOutOfBoundsException();
	        		}	
	        		i++;
			}
	      	}
	      	
	      	/**
	    	 * Template function to check array length.
	    	 */
	    	template<typename T, typename U>
		static void length(__JavaArray<T>* __this, vector<int32_t> lengths) {
			
		}
		
	    	/**
	    	 * Template function to check array stores.
	    	 */
	    	template<typename T, typename U>
	    	void __checkArrayStore(__JavaArray<T>* array, U object) {
	      		if (0 != object) {
	        		Class arraytype = array->__vptr->getClass(array);
	        		Class eltype = arraytype->__vptr->getComponentType(arraytype);

	      		  	if (! eltype->__vptr->isInstance(eltype, (Object)object)) {
	      		    		throw ArrayStoreException();
	      		  	}
	      		}
	    	}
	}
}

