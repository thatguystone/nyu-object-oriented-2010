#include <stdarg.h>
#include <cstring>
#define ARRAY_T __rt::Ptr<java::util::__Array<T> >
#define ARRAY(t) __rt::Ptr<java::util::__Array<t> >

template <typename T>
struct __Array {
	//Field layout
	__Array_VT<T>* __vptr;
	static java::lang::Class __class();
	
	//the number of dimensions
	int32_t __dim;
	
	//the actual dimensions
	int32_t* __dims;
	
	//the data in the array
	T* __data;
	
	//Constructors
	__Array() :
		__vptr(&__vtable) {
			ARRAY_T __this(this, true);
			__CONSTRUCTOR__Array(__this);
		};
		
	__Array(int32_t dim, ...) :
		__vptr(&__vtable), __dim(dim) {
			__dims = new int32_t[__dim];
			
			int32_t cppLength = 1;
			
			//start pulling in our length
			va_list args; 
			va_start(args, dim);
			for (int32_t i = 0; i < __dim; i++) {
				__dims[i] = va_arg(args, int32_t);
				cppLength *= __dims[i];
			}
			
			//create the array
			__data = new T[cppLength];
			
			//null out the array
			std::memset(__data, 0, cppLength * sizeof(T));
		};
	
	static void __CONSTRUCTOR__Array(ARRAY_T);
	
	T get(int32_t dim, ...);
	
	//Extra Constructors
	
	//Methods
	static void __delete(__Array* __this) {
		delete[] __this->__data;
		delete[] __this->__dims;
		delete __this;
	}

	
	//The vtable for __Array
	static __Array_VT<T> __vtable;
};

template <typename T>
struct __Array_VT {
	java::lang::Class __isa;
	void (*__delete)(__Array<T>*);
	java::lang::Class (*getClass)(ARRAY_T);
	bool (*equals)(ARRAY_T, java::lang::Object);
	int32_t (*hashCode)(ARRAY_T);
	java::lang::String (*toString)(ARRAY_T);
	void (*__CONSTRUCTOR__Object)(ARRAY_T);
	void (*__CONSTRUCTOR__Array)(ARRAY_T);
	
	__Array_VT() :
		__isa(__Array<T>::__class()),
		__delete(&__Array<T>::__delete),
		getClass((java::lang::Class(*)(ARRAY_T))&java::lang::__Object::getClass),
		equals((bool(*)(ARRAY_T, java::lang::Object))&java::lang::__Object::equals),
		hashCode((int32_t(*)(ARRAY_T))&java::lang::__Object::hashCode),
		toString((java::lang::String(*)(ARRAY_T))&java::lang::__Object::toString),
		__CONSTRUCTOR__Object((void(*)(ARRAY_T))&java::lang::__Object::__CONSTRUCTOR__Object),
		__CONSTRUCTOR__Array(&java::util::__Array<T>::__CONSTRUCTOR__Array) {
		};

};
