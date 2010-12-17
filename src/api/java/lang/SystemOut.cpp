void __SystemOut::{overload:print[String]}(SystemOut __this, String s) {
	std::cout << s->__data;
}

void __SystemOut::{overload:print[int]}(SystemOut __this, int32_t s) {
	std::cout << s;
}

void __SystemOut::{overload:print[char]}(SystemOut __this, char_t c) {
	std::cout << c;
}

void __SystemOut::{overload:println[]}(SystemOut __this) {
	std::cout << std::endl;
}

void __SystemOut::{overload:println[String]}(SystemOut __this, String s) {
	std::cout << s->__data << std::endl;
}

void __SystemOut::{overload:println[int]}(SystemOut __this, int32_t i) {
	std::cout << i << std::endl;
}

void __SystemOut::{overload:println[char]}(SystemOut __this, char_t c) {
	std::cout << c << std::endl;
}
