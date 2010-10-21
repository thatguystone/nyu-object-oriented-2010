#include "helloWorld.h"
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
		std::string __Class::getName(java::lang::Class __this) {
			return __this->name;
		};

		java::lang::Class __Class::__class() {
			static java::lang::Class k = new java::lang::__Class();;
			return k;
		};

		__Class_VT __Class::__vtable;
	};

};

namespace defaultPkg {
	void __helloWorld::hello(defaultPkg::helloWorld __this) {
		java::lang::Class cls = __this->__vptr->getClass(__this);
		java::lang::__System::out->println("hello world");
		defaultPkg::test t = new defaultPkg::__test();
		t->__vptr->test(t);
		java::lang::__System::out->println(t->__vptr->ret(t));
		java::lang::__System::out->println(t->__vptr->intRet(t));
		java::lang::__System::out->println(t->__vptr->passArg(t, 1));
	};

	void __helloWorld::main() {
		defaultPkg::helloWorld h = new defaultPkg::__helloWorld();
		int32_t i;
		int32_t x = 1;
		for(i=0;(i<10);(i++)) {
			x=(x*2);
		};

		bool hh = true;
		if(hh) {
			java::lang::__System::out->println("IF STATEMENT!");
		};

		i=0;
		while((i<10)) {
			java::lang::__System::out->print("Number : ");
			java::lang::__System::out->println(i);
			(i++);
		};

		java::lang::__System::out->println(x);
		h->__vptr->hello(h);
	};

	java::lang::Class __helloWorld::__class() {
		static java::lang::Class k = new java::lang::__Class();;
		return k;
	};

	__helloWorld_VT __helloWorld::__vtable;
};

namespace defaultPkg {
	void __test::test(defaultPkg::test __this) {
		java::lang::__System::out->println("Hello from Mr. Test");
	};

	std::string __test::ret(defaultPkg::test __this) {
		return "Hey sexy.";
	};

	int32_t __test::intRet(defaultPkg::test __this) {
		return 1;
	};

	int32_t __test::passArg(defaultPkg::test __this, int32_t i) {
		return i;
	};

	java::lang::Class __test::__class() {
		static java::lang::Class k = new java::lang::__Class();;
		return k;
	};

	__test_VT __test::__vtable;
};

namespace java {
	namespace lang {
		java::lang::Class __VMManager::__class() {
			static java::lang::Class k = new java::lang::__Class();;
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
			static java::lang::Class k = new java::lang::__Class();;
			return k;
		};

		__Object_VT __Object::__vtable;
	};

};

int main() {
	defaultPkg::__helloWorld::main();
	
	return 0;
};

