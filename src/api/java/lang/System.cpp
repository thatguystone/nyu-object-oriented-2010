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
