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

namespace defaultPkg {
	struct __test4;
	struct __test4_VT;
	
	typedef __test4* test4;
};

namespace java {
	namespace lang {
		struct __String;
		struct __String_VT;
		
		typedef __String* String;
	};

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

namespace defaultPkg {
	struct __test4 {
		__test4_VT* __vptr;
		
		__test4() : __vptr(&__vtable) {};
		static java::lang::Class __class();
		
		static void main();
		
		private:
		static __test4_VT __vtable;
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

namespace defaultPkg {
	struct __test4_VT {
		java::lang::Class __isa;
		java::lang::Class (*getClass)(defaultPkg::test4);
		int32_t (*hashCode)(defaultPkg::test4);
		bool (*equals)(defaultPkg::test4, java::lang::Object);
		std::string (*toString)(defaultPkg::test4);
		
		__test4_VT() :
			__isa(__test4::__class()),
			getClass((java::lang::Class(*)(defaultPkg::test4))&java::lang::__Object::getClass),
			hashCode((int32_t(*)(defaultPkg::test4))&java::lang::__Object::hashCode),
			equals((bool(*)(defaultPkg::test4, java::lang::Object))&java::lang::__Object::equals),
			toString((std::string(*)(defaultPkg::test4))&java::lang::__Object::toString) {
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

