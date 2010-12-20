namespace __rt {
	java::lang::Object null(0);
	
	template<typename Target, typename Source>
	Target javaCast(Source other) {
		java::lang::Class k = Target::value_t::__class();

		if (!k->__vptr->isInstance(k, other))
			throw java::lang::ClassCastException();
			
		return Target(other);
	}
	
	template<typename T>
	T* Ptr<T>::operator->() const {
		if (addr == 0)
			throw java::lang::NullPointerException();
		return addr;
	}
	
	template<typename T>
	Ptr<T>::operator char*() {
		if (addr == 0)
			throw java::lang::NullPointerException();
		//be sure to give him a constructor-smart-pointer so that he doesn't trash things when
		//only used for a temporary
		return const_cast<char*>(addr->__vptr->toString(Ptr<T>(addr, true))->__data.data());
	}
}

