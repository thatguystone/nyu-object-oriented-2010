#include "ClassParser.h"
#include <iostream>

namespace fun {

	struct __Object;
	typedef __Object* Object;
	struct __Object {
		
		__Object(){};
	};
	
	struct __Happy;
	typedef __Happy* Happy;
	struct __Happy {
		
		__Happy(){};
	};
	
	
}
int main(void) {
	using namespace fun;
	ClassParser* p = new ClassParser();
	int i = 0;
	String classpath = p->getJavaClassName<int>();
	std::cout << classpath << std::endl;
	return(0);
}	
	
