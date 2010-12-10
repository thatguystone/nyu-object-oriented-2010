java::lang::Class __VMManager::getClassNative(java::lang::Object o) {
	return o->__vptr->__isa;
}
		
int32_t __VMManager::getHashCodeNative(java::lang::Object o) {
	return (int32_t)(intptr_t)o.raw();
}
