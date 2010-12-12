namespace __rt {
	template<typename T>
	class Ptr {
		T* addr;
		size_t* counter;

		public:
		typedef T value_t;

		inline Ptr(T* addr = 0)
		: addr(addr), counter(new size_t(1)) { }

		inline Ptr(const Ptr& other)
		: addr(other.addr), counter(other.counter) {
			++(*counter);
		}

		inline ~Ptr() {
			if (0 == --(*counter)) {
				if (0 != addr)
					addr->__vptr->__delete(addr);
				delete counter;
			}
		}

		inline Ptr& operator=(const Ptr& right) {
			if (addr != right.addr) {
				if (0 == --(*counter)) {
					if (0 != addr)
						addr->__vptr->__delete(addr);
					delete counter;
				}
					
				addr = right.addr;
				counter = right.counter;
				++(*counter);
			}
			return *this;
		}

		inline T& operator*()  const { return *addr; }
		inline T* operator->() const { return addr;  }
		inline T* raw()        const { return addr;  }

		template<typename U>
		friend class Ptr;

		template<typename U>
		inline Ptr(const Ptr<U>& other)
		: addr((T*)other.addr), counter(other.counter) {
			++(*counter);
		}

		template<typename U>
		inline bool operator==(const Ptr<U>& other) const {
			return addr == (T*)other.addr;
		}

		template<typename U>
		inline bool operator!=(const Ptr<U>& other) const {
			return addr != (T*)other.addr;
		}
	};
	
	template<typename T>
	void __delete(T* ptr) {
		delete ptr;
	}
}

