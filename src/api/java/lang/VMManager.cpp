namespace java {
	namespace lang {
		Class VMManager::getClass(Object o) {
			return o->__vptr->__isa;
		}
				
		int32_t VMManager::getHashCode(Object o) {
			return (int32_t)(intptr_t)o;
		}
	}
}
