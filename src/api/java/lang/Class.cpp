bool __Class::isInstance(java::lang::Class __this, java::lang::Object o) {
	java::lang::Class k = o->__vptr->getClass(o);

	do {
		if (__this->__vptr->equals(__this, k))
			return true;

		k = k->__vptr->getSuperclass(k);
	} while (__rt::null != k);

	return false;
}

java::lang::Class __Class::getSuperclass(java::lang::Class __this) {
	return __this->parent;
}

