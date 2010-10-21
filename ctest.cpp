#include "ctest.h"
namespace java {
	namespace lang {
		java::lang::Class __VMManager::getClassNative(Object o) {
			return o->__vptr->__isa;
		}
				
		int32_t __VMManager::getHashCodeNative(Object o) {
			return (int32_t)(intptr_t)o;
		}
	}
}
template<class t>
void java::lang::SystemOut::println(t s) {
	std::cout << s << std::endl;
}

void java::lang::SystemOut::println() {
	std::cout << std::endl;
}

template<class t>
void java::lang::SystemOut::print(t s) {
	std::cout << s;
}
namespace java {
	namespace lang {
		 __Class::__Class( std::string name, java::lang::Class parent, bool prim) : __vptr(&__vtable) {
			this->name=name;
			this->parent=parent;
			this->primitive=prim;
		};

		std::string __Class::getName(java::lang::Class __this) {
			return __this->name;
		};

		java::lang::Class __Class::__class() {
			static java::lang::Class k = new java::lang::__Class("java.lang.Class", NULL, 0);;
			return k;
		};

		__Class_VT __Class::__vtable;
	};

};

namespace java {
	namespace lang {
		java::lang::Class __VMManager::__class() {
			static java::lang::Class k = new java::lang::__Class("java.lang.VMManager", NULL, 0);;
			return k;
		};

		__VMManager_VT __VMManager::__vtable;
	};

};

namespace java {
	namespace lang {
		java::lang::Class __Object::getClass(java::lang::Object __this) {
			return java::lang::__VMManager::getClassNative(__this);
		};

		int32_t __Object::hashCode(java::lang::Object __this) {
			return java::lang::__VMManager::getHashCodeNative(__this);
		};

		bool __Object::equals(java::lang::Object __this, java::lang::Object o) {
			return (__this==o);
		};

		std::string __Object::toString(java::lang::Object __this) {
			java::lang::Class cls = __this->__vptr->getClass(__this);
			return "String";
		};

		java::lang::Class __Object::__class() {
			static java::lang::Class k = new java::lang::__Class("java.lang.Object", NULL, 0);;
			return k;
		};

		__Object_VT __Object::__vtable;
	};

};

namespace defaultPkg {
	void __test4::main() {
		int32_t i;
		for(i=0;(i<10);(i++)) {
			if(((i%2)==1)) {
				java::lang::__System::out->print(i);
				java::lang::__System::out->println(" is even");
			}

			else {
				java::lang::__System::out->print(i);
				java::lang::__System::out->println(" is odd");
			};

		};

	};

	java::lang::Class __test4::__class() {
		static java::lang::Class k = new java::lang::__Class("default.test4", NULL, 0);;
		return k;
	};

	__test4_VT __test4::__vtable;
};

namespace java {
	namespace lang {
		java::lang::Class __String::__class() {
			static java::lang::Class k = new java::lang::__Class("java.lang.String", NULL, 0);;
			return k;
		};

		__String_VT __String::__vtable;
	};

};

int main() {
	defaultPkg::__test4::main();
	
	return 0;
};

