#include <stdarg.h>
#include <cstring>
#include <stdlib.h>
#include <stdio.h>
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
	
	//the sub-arrays, if we have any
	ARRAY(T)* __data;
	
	//the data in the array, if we don't have sub-arrays
	T* __arrayData;
	
	//Constructors
	__Array() :
		__vptr(&__vtable) {
		};
		
	__Array(int32_t dim, ...) :
		__vptr(&__vtable) {
			int32_t* dims = new int32_t[dim];
			//start pulling in our dimensions
			va_list args; 
			va_start(args, dim);
			for (int32_t i = 0; i < dim; i++){
				dims[i] = va_arg(args, int32_t);
			}
			init(dim,dims);
	}
	//Extra Constructor
	__Array(int32_t dim, int32_t* dims) :
		__vptr(&__vtable) {
		init(dim,dims);
	}

	void init(int32_t dim, int32_t* dims){
                __dim = dim;
                __dims = dims;

                if (__dim == 1) {
                        __arrayData = (T*)malloc(__dims[0] * sizeof(T));
                        //null out the array
                        std::memset(__arrayData, 0, __dims[0] * sizeof(T));
                } else {
                        int32_t* newDims = new int32_t[__dim - 1];

                        for (int32_t i = 1; i < __dim; i++)
                                newDims[i-1] = dims[i];

//                      ARRAY(T)* tmp = new ARRAY(T)[__dims[0]];
                        __data=new ARRAY(T)[__dims[0]];
                        for (int32_t i = 0; i < __dims[0]; i++){
                                __data[i] = new __Array<T>(__dim - 1, newDims);
                        }
//                      __data = tmp;

                        delete[] newDims;
                }
	}

	static void __CONSTRUCTOR__Array(ARRAY_T);
	
	T& get(int32_t dim, ...);
	
	ARRAY(T) getMulti(int32_t dim, ...);
	
	//Methods
	static void __delete(__Array* __this) {
		delete[] __this->__data;
		delete[] __this->__arrayData;
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
