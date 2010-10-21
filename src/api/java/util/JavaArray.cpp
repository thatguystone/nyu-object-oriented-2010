/**
 * Array.cc contains the template specialization for arrays using C++ templates.
 */

#include "JavaArray.h"

namespace java {
	namespace util {
 
		// Nothing in here  
		static Class __class() {
			 static Class k = new __Class("[I",
                                   __Object::__class(),
                                   __Integer::__primitiveClass());
      			 return k;
		}
	}
}

