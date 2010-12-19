template <typename T>
T __Array<T>::get(int32_t dim, ...) {
	//see if we're doing something illegal
	if (dim > __dim)
		throw java::lang::ArrayIndexOutOfBoundsException();
	
	va_list args; 
	va_start(args, dim);
	
	int32_t offset = 1;
	
	//check to see that all our dimensions are in bounds
	for (int32_t i = 0; i < dim; i++) {
		int32_t d = va_arg(args, int32_t);
		
		//if we're accessing something beyond our reach
		if (d < 0 || d > __dims[i])
			throw java::lang::ArrayIndexOutOfBoundsException();
		
		offset *= d;
	}
	
	if (dim < __dim)
		std::cout << "Multi-dimensional array with fewer dimensional access." << std::endl;
	
	return __data[offset - 1];
}
