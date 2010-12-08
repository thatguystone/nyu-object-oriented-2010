java::lang::Class __VMManager::getClassNative(Object o) {
	return o->__vptr->__isa;
}
		
int32_t __VMManager::getHashCodeNative(Object o) {
	return (int32_t)(intptr_t)o;
}
