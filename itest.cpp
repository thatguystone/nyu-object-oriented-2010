#include "itest.h"
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

namespace java {
	namespace lang {
		java::lang::Class __String::__class() {
			static java::lang::Class k = new java::lang::__Class("java.lang.String", NULL, 0);;
			return k;
		};

		__String_VT __String::__vtable;
	};

};

namespace defaultPkg {
	 __needsInherit::__needsInherit() : __vptr(&__vtable) {
	};

	void __needsInherit::printCls(defaultPkg::needsInherit __this) {
		java::lang::__System::out->println("I am a needsInherit object");
	};

	java::lang::Class __needsInherit::__class() {
		static java::lang::Class k = new java::lang::__Class("default.needsInherit", NULL, 0);;
		return k;
	};

	__needsInherit_VT __needsInherit::__vtable;
};

namespace defaultPkg {
	void __test1::main() {
		defaultPkg::needsInherit inheritor = new defaultPkg::__needsInherit();
		inheritor->__vptr->printCls(inheritor);
		inheritor->__vptr->printInherit(inheritor);
	};

	java::lang::Class __test1::__class() {
		static java::lang::Class k = new java::lang::__Class("default.test1", NULL, 0);;
		return k;
	};

	__test1_VT __test1::__vtable;
};

namespace defaultPkg {
	void __inheritMe::printCls(defaultPkg::inheritMe __this) {
		java::lang::__System::out->println("I am a inheritMe object");
	};

	void __inheritMe::printInherit(defaultPkg::inheritMe __this) {
		java::lang::__System::out->println();
		java::lang::__System::out->println("You have successfully demonstrated inheritance!");
		java::lang::__System::out->println();
	};

	java::lang::Class __inheritMe::__class() {
		static java::lang::Class k = new java::lang::__Class("default.inheritMe", NULL, 0);;
		return k;
	};

	__inheritMe_VT __inheritMe::__vtable;
};

int main() {
	defaultPkg::__test1::main();
	
	return 0;
};

