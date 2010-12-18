namespace __rt {
	java::lang::Object null(0);
	
	template<typename Target, typename Source>
	Target javaCast(Source other) {
		java::lang::Class k = Target::value_t::__class();

		if (!k->__vptr->isInstance(k, other))
			throw java::lang::ClassCastException();

		return Target(other);
	}
}

