namespace __rt {
	template<typename T>
	class Ptr {
		T* addr;
		size_t* counter;
		bool* inConstructor;

		public:
		typedef T value_t;

		inline Ptr(T* addr = 0)
		: addr(addr), counter(new size_t(1)), inConstructor(new bool(false)) { }
		
		/**
		 * Handles making sure that we don't delete a newly-created object if we're a smart pointer in a constructor
		 */
		inline explicit Ptr(T* addr, bool inConstructor)
		: addr(addr), counter(new size_t(1)), inConstructor(new bool(inConstructor)) { }

		inline Ptr(const Ptr& other)
		: addr(other.addr), counter(other.counter), inConstructor(new bool(false)) {
			++(*counter);
		}

		inline ~Ptr() {
			if (0 == --(*counter)) {
				if (!(*inConstructor) && 0 != addr)
					addr->__vptr->__delete(addr);
				delete counter;
				delete inConstructor;
			}
		}

		inline Ptr& operator=(const Ptr& right) {
			if (addr != right.addr) {
				if (0 == --(*counter)) {
					if (!(*inConstructor) && 0 != addr)
						addr->__vptr->__delete(addr);
					delete counter;
					delete inConstructor;
				}
					
				addr = right.addr;
				counter = right.counter;
				inConstructor = right.inConstructor;
				++(*counter);
			}
			return *this;
		}

		inline T& operator*()  const { return *addr; }
		inline T* raw()        const { return addr; }
		inline T* operator->() const;

		template<typename U>
		friend class Ptr;

		template<typename U>
		inline Ptr(const Ptr<U>& other)
		: addr((T*)other.addr), counter(other.counter), inConstructor(other.inConstructor) {
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
		
		operator char*() {
			//be sure to give him a constructor-smart-pointer so that he doesn't trash things when
			//only used for a temporary
			return const_cast<char*>(addr->__vptr->toString(Ptr<T>(addr, true))->__data.data());
		}
	};
	
	template<typename T>
	void __delete(T* ptr) {
		delete ptr;
	}
}

