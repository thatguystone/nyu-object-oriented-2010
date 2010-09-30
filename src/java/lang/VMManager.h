#pragma once

namespace java {
	namespace lang {
		class VMManager {
			public:
				static Class getClass(Object o);
				static int32_t getHashCode(Object o);
		};
	}
}
