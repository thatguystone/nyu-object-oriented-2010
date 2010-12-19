bool __Class::isInstance(java::lang::Class __this, java::lang::Object o) {
	java::lang::Class testing = __this;
	java::lang::Class lookingFor = o->__vptr->getClass(o);
	
	do {
		if (lookingFor == testing)
			return true;

		testing = testing->__vptr->getSuperclass(testing);
	} while (testing != __rt::null);

	return false;
}

java::lang::Class __Class::getSuperclass(java::lang::Class __this) {
	return __this->parent;
}

String __Class::toString(Class __this) {
	if (__this->primitive) {
		return __this->name;
	} else {
		return java::lang::asString("class " + __this->name->__data);
	}
}
