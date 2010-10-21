/**
 * Array.h contains the declarations for generic arrays using C++ templates.
 * Creates the Array and Array_VT interface 
 */
 
#pragma once

#include <stdint.h>
#include <string>
#include <cstring>
#include "java_lang.h"
#include "ClassParser.h"

namespace java {
	namespace util {

		using namespace java::lang;

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
		struct __Array;

		template <typename T>
		struct __Array_VT;
	
		/**
		 * Definition of convenient type names.
	    	 */
		//typedef __Array<T>* ArrayOfType;
	    	//typedef __Array_VT<T>* Array_VT;
    	
	    	/**
	    	 * The data layout for arrays.
		 */
		template <typename T>
		struct __Array {
			__Array_VT<T>* __vptr;		// pointer to the vtable
			const int32_t length;		// length of the array
			T* __data;			// pointer to the array holding our data

			/**
			 * Constructor for Array
			 */
			__Array(const int32_t length) 
			: __vptr(&__vtable), length(length), __data(new T[length]) {
				std::memset(__data, 0, length * sizeof(T));
			}
		
			/**
			 * Returns Class object.  Implemented in Array.cc
			 */
	      		static Class __class();
		
			/**
			 * private vtable.  1 per class
			 */
	    		private:
	      			static __Array_VT<T> __vtable;
	    	};

	    	/**
	    	 * The vtable layout for arrays.
	    	 */
	    	template <typename T>
	    	struct __Array_VT {
	      		typedef __Array<T>* Array;
	      		Class __isa;
	      		int32_t (*hashCode)(Array);
	      		bool (*equals)(Array, Object);
	      		Class (*getClass)(Array);
	      		String (*toString)(Array);
  
	      		/**
	      		 * Constructor for Array_VT
	      		 */
	      		__Array_VT()
	      		: __isa(__Array<T>::__class()),
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
    		__Array_VT<T> __Array<T>::__vtable;

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
		static void set(__Array<T>* __this, int32_t index, T value) {
			__Array<T>::checkIndex(__this, index);        
			__this->__data[index] = value;
	      	}
	
		/**
		 * Template function to get an element in an array
		 */
		template <typename T>
	      	static T get(__Array<T>* __this, int32_t index) {
			__Array<T>::checkIndex(__this, index);        
			return __this->__data[index];
	      	}
	
		/**
		 * Template function to check if an index is out of bounds
		 */
		template <typename T>
	      	static void checkIndex(__Array<T>* __this, int32_t index) {
	        	if (0 > index || index >= __this->length) {
	          		throw ArrayIndexOutOfBoundsException();
	        	}
	      	}
    
	    	/**
	    	 * Template function to check array stores.
	    	 */
	    	template<typename T, typename U>
	    	void __checkArrayStore(__Array<T>* array, U object) {
	      		if (0 != object) {
	        		Class arraytype = array->__vptr->getClass(array);
	        		Class eltype = arraytype->__vptr->getComponentType(arraytype);

	      		  	if (! eltype->__vptr->isInstance(eltype, (Object)object)) {
	      		    		throw ArrayStoreException();
	      		  	}
	      		}
	    	}
	    	
	    	/**
	    	 * In Grimm's code we would use template specialization to implement the 
	    	 * Array< >::_class() method, however every new class that gets an array
	    	 * instantiation would require a new template specialization at runtime.  
	    	 * To avoid this we create one generic method using the ClassParser class
	    	 * so that we never have to write new C++ at runtime
	    	 */ 
    		template<typename T>
    		Class __Array<T>::__class() {
    			//Object o = new T();
    			ClassParser* p = new ClassParser();
    			String name = p->getJavaClassName<T>();
      			static Class k = new __Class(name,
                		__Object::__class(),
                	        __Integer::__primitiveClass());
      				return k;
    		}
	}
}

