#include <iostream>
#include <string>
#include <stdlib.h>

namespace java {
	namespace lang {
		class SystemOut {
			public:
				template <class t>
				static void println(t s);
				
				static void println();
				
				template <class t>
				static void print(t s);
				
				static void print() {
					; //print nothing......
				}
		};
		
		class __System {
			public:
				static SystemOut out;
		};
	}
}
