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

namespace defaultPkg {
	 __test3::__test3() : __vptr(&__vtable) {
		this->x = 3;
	};

	void __test3::willIWork(defaultPkg::test3 __this) {
		java::lang::__System::out->println(__this->__vptr->getVal(__this));
	};

	java::lang::Class __test3::__class() {
		static java::lang::Class k = new java::lang::__Class("default.test3", NULL, 0);;
		return k;
	};

	__test3_VT __test3::__vtable;
};

namespace defaultPkg {
	 __test2::__test2() : __vptr(&__vtable) {
	};

	int32_t __test2::getVal(defaultPkg::test2 __this) {
		return 35463;
	};

	java::lang::Class __test2::__class() {
		static java::lang::Class k = new java::lang::__Class("default.test2", NULL, 0);;
		return k;
	};

	__test2_VT __test2::__vtable;
};

namespace defaultPkg {
	void __helloWorld::hello(defaultPkg::helloWorld __this) {
		java::lang::Class cls = __this->__vptr->getClass(__this);
		java::lang::__System::out->println("hello world");
		defaultPkg::test t = new defaultPkg::__test();
		java::lang::__System::out->println(t->__vptr->ret(t));
		java::lang::__System::out->println(t->__vptr->intRet(t));
		java::lang::__System::out->print("n from test: ");
		t->__vptr->printN(t);
		java::lang::__System::out->print("m from test: ");
		defaultPkg::__test::printM();
		java::lang::__System::out->println(t->__vptr->passArg(t, 1));
	};

	void __helloWorld::main() {
		defaultPkg::helloWorld h = new defaultPkg::__helloWorld();
		int32_t i;
		int32_t x = 1;
		for(i=0;(i<10);(i++)) {
			if((i>4)) {
				x=(x*2);
				java::lang::__System::out->print(i);
				java::lang::__System::out->print(" -- ");
				java::lang::__System::out->println(x);
			};

		};

		defaultPkg::test2 d = new defaultPkg::__test2();
		defaultPkg::test3 e = new defaultPkg::__test3();
		e->__vptr->willIWork(e);
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
		defaultPkg::__test::staticSomething();
		defaultPkg::__test::staticSomethingWithArgs('a');
		java::lang::__System::out->println(defaultPkg::__test::staticSomethingReturnWithArgs('b'));
		java::lang::__System::out->println(defaultPkg::__test::staticSomethingReturnWith2Args(1, 2));
		java::lang::__System::out->println(defaultPkg::__test::staticSomethingReturnWith5Args(1, 2, 4, 5, 6));
	};

	java::lang::Class __helloWorld::__class() {
		static java::lang::Class k = new java::lang::__Class("default.helloWorld", NULL, 0);;
		return k;
	};

	__helloWorld_VT __helloWorld::__vtable;
};

namespace defaultPkg {
	int32_t __test::m = 1;
	 __test::__test() : __vptr(&__vtable) {
		this->ss = "hi";
		java::lang::__System::out->println("Hello from Mr. Test");
	};

	std::string __test::ret(defaultPkg::test __this) {
		__this->n=2;
		__this->n=(__this->n+2);
		return "Hey sexy.";
	};

	int32_t __test::intRet(defaultPkg::test __this) {
		return 1;
	};

	int32_t __test::passArg(defaultPkg::test __this, int32_t i) {
		return i;
	};

	void __test::printN(defaultPkg::test __this) {
		java::lang::__System::out->println(__this->n);
	};

	void __test::staticSomething() {
		java::lang::__System::out->println("Call from static!");
	};

	void __test::staticSomethingWithArgs(char a) {
		java::lang::__System::out->println(a);
	};

	char __test::staticSomethingReturnWithArgs(char a) {
		return a;
	};

	int32_t __test::staticSomethingReturnWith2Args(int32_t a, int32_t b) {
		return (a+b);
	};

	int32_t __test::staticSomethingReturnWith5Args(int32_t a, int32_t b, int32_t c, int32_t d, int32_t e) {
		return ((((a*b)+c)+d)+e);
	};

	void __test::printM() {
		java::lang::__System::out->println(m);
	};

	java::lang::Class __test::__class() {
		static java::lang::Class k = new java::lang::__Class("default.test", NULL, 0);;
		return k;
	};

	__test_VT __test::__vtable;
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

int main() {
	defaultPkg::__helloWorld::main();
	
	return 0;
};

