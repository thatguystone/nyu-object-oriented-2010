template <typename T>
T& __Array<T>::get(int32_t dim, ...) {
	//see if we're doing something illegal
	if (dim != __dim)
		throw java::lang::ArrayIndexOutOfBoundsException();
	
	va_list args; 
	va_start(args, dim);
	
	ARRAY(T)* find = __data;
	
	int32_t d;
	for (int32_t i = 0; i < dim; i++) {
		d = va_arg(args, int32_t);
		
		//if we're accessing something beyond our reach
		if (d < 0 || d > __dims[i])
			throw java::lang::ArrayIndexOutOfBoundsException();
		
		find = &find[i];
	}
	
	return (*find)->__arrayData[d];
}

template <typename T>
ARRAY(T) __Array<T>::getMulti(int32_t dim, ...) {
	//see if we're doing something illegal
	if (dim > __dim)
		throw java::lang::ArrayIndexOutOfBoundsException();
	
	va_list args;
	va_start(args, dim);
	
	int32_t offset = 1;
	
	ARRAY(T)* ret = __data;
			
	//check to see that all our dimensions are in bounds
	for (int32_t i = 0; i < dim; i++) {
		int32_t d = va_arg(args, int32_t);
		
		//if we're accessing something beyond our reach
		if (d < 0 || d > __dims[i])
			throw java::lang::ArrayIndexOutOfBoundsException();
		
		ret = &ret->raw()->__data[i];
	}
	
	return *ret;
}
