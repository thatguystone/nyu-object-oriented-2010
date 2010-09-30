#include <stdint.h>

class VMManager {
	public:
		static Class getClass(Object o) {
			return o->__vptr->__isa;
		}
		
		static int32_t getHashCode(Object o) {
			return (int32_t)(intptr_t)o;
		}
}
