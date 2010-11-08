#include <stdint.h>
#include <iostream>
#include <sstream>
#include <string>
#include <stdlib.h>
#include <stdint.h>

class String : public std::string {
	typedef std::string string;
	
	public:
	
	String() : string() { }
	String(const string& str) : string(str) { }
	String(const string& str, size_t pos, size_t n = npos) : string(str, pos, n) { }
	String(const char * s, size_t n) : string(s, n) { } 
	String(const char * s) : string(s) { }
	String(size_t n, char c) : string(n, c) { }
	
	String& operator+(int i) {
		String *t = new String("Test");
		return *t;
	}
	
	void $h() {
		
	}
};

int main() {
	({"foo";});
	
	//("foo" + 4).length()	
	({
		std::ostringstream sout;
		sout << "foo" << 4;
		sout.str();
	});
	
	std::cout << ((String)"" + 7 + (String)"test") << std::endl;
}
