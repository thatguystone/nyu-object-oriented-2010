#include <sstream>

java::lang::String operator+(java::lang::String a, java::lang::String b) {
	return a->__vptr->concat(a, b);
}

template <typename T>
java::lang::String operator+(java::lang::String a, T b) {
	std::ostringstream sout;
	sout << a->__data << b;
	return new java::lang::__String(sout.str());
}

