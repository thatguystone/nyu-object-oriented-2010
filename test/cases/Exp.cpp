#include <stdint.h>
#include <iostream>
#include <sstream>
#include <string>
#include <stdlib.h>

int main() {
	({"foo";});
	
	//("foo" + 4).length()	
	({
		std::ostringstream sout;
		sout << "foo" << 4;
		sout.str();
	});
}
