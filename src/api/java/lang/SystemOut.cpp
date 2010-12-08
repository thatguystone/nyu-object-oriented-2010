void __SystemOut::{overload:print[String]}(String s) {
	std::cout << s;
}

void __SystemOut::{overload:println[]}() {
	std::cout << std::endl;
}

void __SystemOut::{overload:println[String]}(String s) {
	std::cout << s << std::endl;
}
