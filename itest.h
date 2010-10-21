#include <stdint.h>
#include <iostream>
#include <string>
#include <stdlib.h>

namespace java {
	namespace lang {
		class SystemOut {
			public:
				template <class t>
				static void println(t s);
				
				static void println();
				
				template <class t>
				static void print(t s);
				
				static void print() {
					; //print nothing......
				}
		};
		
		class __System {
			public:
				static SystemOut* out;
		};
	}
}
namespace java {
	namespace lang {
		struct __Class;
		struct __Class_VT;
		
		typedef __Class* Class;
	};

};

namespace java {
	namespace lang {
		struct __VMManager;
		struct __VMManager_VT;
		
		typedef __VMManager* VMManager;
	};

};

namespace java {
	namespace lang {
		struct __Object;
		struct __Object_VT;
		
		typedef __Object* Object;
	};

};

namespace java {
	namespace lang {
		struct __String;
		struct __String_VT;
		
		typedef __String* String;
	};

};

namespace defaultPkg {
	struct __needsInherit;
	struct __needsInherit_VT;
	
	typedef __needsInherit* needsInherit;
};

namespace defaultPkg {
	struct __test1;
	struct __test1_VT;
	
	typedef __test1* test1;
};

namespace defaultPkg {
	struct __inheritMe;
	struct __inheritMe_VT;
	
	typedef __inheritMe* inheritMe;
};

namespace java {
	namespace lang {
		struct __Class {
			__Class_VT* __vptr;
			
			__Class( std::string name, java::lang::Class parent, bool prim);
			static java::lang::Class __class();
			std::string name;
			java::lang::Class parent;
			bool primitive;
			
			static std::string getName(java::lang::Class);
			
			private:
			static __Class_VT __vtable;
		};

	};

};

namespace java {
	namespace lang {
		struct __VMManager {
			__VMManager_VT* __vptr;
			
			__VMManager() : __vptr(&__vtable) {};
			static java::lang::Class __class();
			
			static java::lang::Class getClassNative(java::lang::Object);
			static int32_t getHashCodeNative(java::lang::Object);
			
			private:
			static __VMManager_VT __vtable;
		};

	};

};

namespace java {
	namespace lang {
		struct __Object {
			__Object_VT* __vptr;
			
			__Object() : __vptr(&__vtable) {};
			static java::lang::Class __class();
			
			static java::lang::Class getClass(java::lang::Object);
			static int32_t hashCode(java::lang::Object);
			static bool equals(java::lang::Object, java::lang::Object);
			static std::string toString(java::lang::Object);
			
			private:
			static __Object_VT __vtable;
		};

	};

};

namespace java {
	namespace lang {
		struct __String {
			__String_VT* __vptr;
			
			__String() : __vptr(&__vtable) {};
			static java::lang::Class __class();
			
			
			private:
			static __String_VT __vtable;
		};

	};

};

namespace defaultPkg {
	struct __needsInherit {
		__needsInherit_VT* __vptr;
		
		__needsInherit();
		static java::lang::Class __class();
		
		static void printCls(defaultPkg::needsInherit);
		
		private:
		static __needsInherit_VT __vtable;
	};

};

namespace defaultPkg {
	struct __test1 {
		__test1_VT* __vptr;
		
		__test1() : __vptr(&__vtable) {};
		static java::lang::Class __class();
		
		static void main();
		
		private:
		static __test1_VT __vtable;
	};

};

namespace defaultPkg {
	struct __inheritMe {
		__inheritMe_VT* __vptr;
		
		__inheritMe() : __vptr(&__vtable) {};
		static java::lang::Class __class();
		
		static void printCls(defaultPkg::inheritMe);
		static void printInherit(defaultPkg::inheritMe);
		
		private:
		static __inheritMe_VT __vtable;
	};

};

namespace java {
	namespace lang {
		struct __Class_VT {
			java::lang::Class __isa;
			java::lang::Class (*getClass)(java::lang::Class);
			int32_t (*hashCode)(java::lang::Class);
			bool (*equals)(java::lang::Class, java::lang::Object);
			std::string (*toString)(java::lang::Class);
			std::string (*getName)(java::lang::Class);
			
			__Class_VT() :
				__isa(__Class::__class()),
				getClass((java::lang::Class(*)(java::lang::Class))&java::lang::__Object::getClass),
				hashCode((int32_t(*)(java::lang::Class))&java::lang::__Object::hashCode),
				equals((bool(*)(java::lang::Class, java::lang::Object))&java::lang::__Object::equals),
				toString((std::string(*)(java::lang::Class))&java::lang::__Object::toString),
				getName(&__Class::getName) {
			};

		};

	};

};

namespace java {
	namespace lang {
		struct __VMManager_VT {
			java::lang::Class __isa;
			java::lang::Class (*getClass)(java::lang::VMManager);
			int32_t (*hashCode)(java::lang::VMManager);
			bool (*equals)(java::lang::VMManager, java::lang::Object);
			std::string (*toString)(java::lang::VMManager);
			java::lang::Class (*getClassNative)(java::lang::Object);
			int32_t (*getHashCodeNative)(java::lang::Object);
			
			__VMManager_VT() :
				__isa(__VMManager::__class()),
				getClass((java::lang::Class(*)(java::lang::VMManager))&java::lang::__Object::getClass),
				hashCode((int32_t(*)(java::lang::VMManager))&java::lang::__Object::hashCode),
				equals((bool(*)(java::lang::VMManager, java::lang::Object))&java::lang::__Object::equals),
				toString((std::string(*)(java::lang::VMManager))&java::lang::__Object::toString),
				getClassNative(&__VMManager::getClassNative),
				getHashCodeNative(&__VMManager::getHashCodeNative) {
			};

		};

	};

};

namespace java {
	namespace lang {
		struct __Object_VT {
			java::lang::Class __isa;
			java::lang::Class (*getClass)(java::lang::Object);
			int32_t (*hashCode)(java::lang::Object);
			bool (*equals)(java::lang::Object, java::lang::Object);
			std::string (*toString)(java::lang::Object);
			
			__Object_VT() :
				__isa(__Object::__class()),
				getClass(&__Object::getClass),
				hashCode(&__Object::hashCode),
				equals(&__Object::equals),
				toString(&__Object::toString) {
			};

		};

	};

};

namespace java {
	namespace lang {
		struct __String_VT {
			java::lang::Class __isa;
			java::lang::Class (*getClass)(java::lang::String);
			int32_t (*hashCode)(java::lang::String);
			bool (*equals)(java::lang::String, java::lang::Object);
			std::string (*toString)(java::lang::String);
			
			__String_VT() :
				__isa(__String::__class()),
				getClass((java::lang::Class(*)(java::lang::String))&java::lang::__Object::getClass),
				hashCode((int32_t(*)(java::lang::String))&java::lang::__Object::hashCode),
				equals((bool(*)(java::lang::String, java::lang::Object))&java::lang::__Object::equals),
				toString((std::string(*)(java::lang::String))&java::lang::__Object::toString) {
			};

		};

	};

};

namespace defaultPkg {
	struct __needsInherit_VT {
		java::lang::Class __isa;
		java::lang::Class (*getClass)(defaultPkg::needsInherit);
		int32_t (*hashCode)(defaultPkg::needsInherit);
		bool (*equals)(defaultPkg::needsInherit, java::lang::Object);
		std::string (*toString)(defaultPkg::needsInherit);
		void (*printCls)(defaultPkg::needsInherit);
		void (*printInherit)(defaultPkg::needsInherit);
		
		__needsInherit_VT() :
			__isa(__needsInherit::__class()),
			getClass((java::lang::Class(*)(defaultPkg::needsInherit))&java::lang::__Object::getClass),
			hashCode((int32_t(*)(defaultPkg::needsInherit))&java::lang::__Object::hashCode),
			equals((bool(*)(defaultPkg::needsInherit, java::lang::Object))&java::lang::__Object::equals),
			toString((std::string(*)(defaultPkg::needsInherit))&java::lang::__Object::toString),
			printCls(&__needsInherit::printCls),
			printInherit((void(*)(defaultPkg::needsInherit))&defaultPkg::__inheritMe::printInherit) {
		};

	};

};

namespace defaultPkg {
	struct __test1_VT {
		java::lang::Class __isa;
		java::lang::Class (*getClass)(defaultPkg::test1);
		int32_t (*hashCode)(defaultPkg::test1);
		bool (*equals)(defaultPkg::test1, java::lang::Object);
		std::string (*toString)(defaultPkg::test1);
		void (*printCls)(defaultPkg::test1);
		void (*printInherit)(defaultPkg::test1);
		
		__test1_VT() :
			__isa(__test1::__class()),
			getClass((java::lang::Class(*)(defaultPkg::test1))&java::lang::__Object::getClass),
			hashCode((int32_t(*)(defaultPkg::test1))&java::lang::__Object::hashCode),
			equals((bool(*)(defaultPkg::test1, java::lang::Object))&java::lang::__Object::equals),
			toString((std::string(*)(defaultPkg::test1))&java::lang::__Object::toString),
			printCls((void(*)(defaultPkg::test1))&defaultPkg::__inheritMe::printCls),
			printInherit((void(*)(defaultPkg::test1))&defaultPkg::__inheritMe::printInherit) {
		};

	};

};

namespace defaultPkg {
	struct __inheritMe_VT {
		java::lang::Class __isa;
		java::lang::Class (*getClass)(defaultPkg::inheritMe);
		int32_t (*hashCode)(defaultPkg::inheritMe);
		bool (*equals)(defaultPkg::inheritMe, java::lang::Object);
		std::string (*toString)(defaultPkg::inheritMe);
		void (*printCls)(defaultPkg::inheritMe);
		void (*printInherit)(defaultPkg::inheritMe);
		
		__inheritMe_VT() :
			__isa(__inheritMe::__class()),
			getClass((java::lang::Class(*)(defaultPkg::inheritMe))&java::lang::__Object::getClass),
			hashCode((int32_t(*)(defaultPkg::inheritMe))&java::lang::__Object::hashCode),
			equals((bool(*)(defaultPkg::inheritMe, java::lang::Object))&java::lang::__Object::equals),
			toString((std::string(*)(defaultPkg::inheritMe))&java::lang::__Object::toString),
			printCls(&__inheritMe::printCls),
			printInherit(&__inheritMe::printInherit) {
		};

	};

};

