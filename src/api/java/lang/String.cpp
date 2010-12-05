namespace java {
	namespace lang {
		class String : public std::string {
			typedef std::string string;
			
			public:
			
			/** Pass on all string constructors to std::string */
			String() : string() { }
			String(const string& str) : string(str) { }
			String(const string& str, size_t pos, size_t n = npos) : string(str, pos, n) { }
			String(const char * s, size_t n) : string(s, n) { } 
			String(const char * s) : string(s) { }
			String(size_t n, char c) : string(n, c) { }
			
			/**
			 * Following the java standard for concat -- make a copy of the string
			 * if adding anything.
			 */
			String {overload:concat[java.lang.String]}(String str) {
				if (str.size() == 0) {
					return *this;
				} else {
					String *ret = new String();
					*ret = *this;
					ret->append(str);
					return *ret;
				}
			}
			
			int32_t {overload:length[]}() {
				return (int)this->size();
			}
			
			bool {overload:contains[java.lang.String]}(String s) {
				return ((int)this->find(s)) > -1;
			}
			
			bool {overload:endsWith[java.lang.String]}(String s) {
				return (this->length() - s.length()) == this->lastIndexOf(s);
			}
			
			int {overload:indexOf[char]}(char ch) {
				return ((int)this->find(ch));
			}
			
			int {overload:indexOf[java.lang.String]}(String str) {
				return ((int)this->find(str));
			}
			
			int {overload:lastIndexOf[char]}(char ch) {
				return ((int)this->rfind(ch));
			}
			
			int {overload:lastIndexOf[java.lang.String]}(String str) {
				return ((int)this->rfind(str));
			}
			
			String {overload:replace[char,char]}(char old, char newChar) {
				String *ret = new String();
				
				int l = this->length();
				for (int i = 0; i < l; i++) {
					if (this->at(i) == old)
						ret->push_back(newChar);
					else
						ret->push_back(this->at(i));
				}
				
				return *ret;
			}
			
			bool {overload:startsWith[java.lang.String]}(String s) {
				return this->indexOf(s) == 0;
			}
			
			String {overload:substring[int]}(int32_t start) {
				return this->substr(start);
			}
			
			String {overload:substring[int]}(int32_t start, int32_t end) {
				return this->substr(start, end - start);
			}
			
			String {overload:toLowerCase[]}() {
				String *ret = new String();
				
				int l = this->length();
				for (int i = 0; i < l; i++) {
					char chr = this->at(i);
					if (chr >= 'A' && chr <= 'Z')
						ret->push_back(chr + ' ');
					else
						ret->push_back(chr);
				}
				
				return *ret;
			}
			
			String {overload:toUpperCase[]}() {
				String *ret = new String();
				
				int l = this->length();
				for (int i = 0; i < l; i++) {
					char chr = this->at(i);
					if (chr >= 'a' && chr <= 'z')
						ret->push_back(chr - ' ');
					else
						ret->push_back(chr);
				}
				
				return *ret;
			}
		};
	}
}
