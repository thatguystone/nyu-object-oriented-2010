template <typename T>
T& __Array<T>::get(int32_t dim, ...) {
	//see if we're doing something illegal
	if (dim != __dim)
		throw java::lang::ArrayIndexOutOfBoundsException();
	
	va_list args; 
	va_start(args, dim);
	
	int32_t d,index;
	if (dim>1){
		ARRAY(T)* find = __data;
		for (int32_t i = 0; i < dim-2; ++i) {
			d = va_arg(args, int32_t);
			//if we're accessing something beyond our reach
			if (d < 0 || d > __dims[i]-1)
				throw java::lang::ArrayIndexOutOfBoundsException();
			find = find[d]->__data;
		}
		d = va_arg(args, int32_t);
		index = va_arg(args, int32_t);
		//if we're accessing something beyond our reach
		if (d < 0 || d > __dims[dim-2]-1)
			throw java::lang::ArrayIndexOutOfBoundsException();
		if (index < 0 || index > __dims[dim-1]-1)
			throw java::lang::ArrayIndexOutOfBoundsException();
		return find[d]->__arrayData[index];
	}
	d = va_arg(args, int32_t);
	//if we're accessing something beyond our reach
	if (d < 0 || d > __dims[0]-1)
		throw java::lang::ArrayIndexOutOfBoundsException();
	return __arrayData[d];

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
