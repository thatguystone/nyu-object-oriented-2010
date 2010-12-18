java::lang::String __Object::{overload:toString[]}(Object __this) {
	Class k = __this->__vptr->getClass(__this);
	std::ostringstream sout;
	sout << k->__vptr->getName(k) << '@' << std::hex << (uintptr_t)__this.raw();
	return new java::lang::__String(sout.str());	
}
