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
			String& <java.lang.String.concat(String)>(String str) {
				if (str.size() == 0) {
					return *this;
				} else {
					String *ret = new String();
					*ret = *this;
					ret->append(str);
					return *ret;
				}
			}
			
			int length() {
				return (int)this->size();
			}
			
			bool contains(String s) {
				return ((int)this->find(s)) > -1;
			}
			
			bool endsWith(String s) {
				return (this->length() - s.length()) == this->lastIndexOf(s);
			}
			
			int indexOf(char ch) {
				return ((int)this->find(ch));
			}
			
			int indexOf(String str) {
				return ((int)this->find(str));
			}
			
			int lastIndexOf(char ch) {
				return ((int)this->rfind(ch));
			}
			
			int lastIndexOf(String str) {
				return ((int)this->rfind(str));
			}
			
			String& replace(char old, char newChar) {
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
			
			bool startsWith(String s) {
				return this->indexOf(s) == 0;
			}
			
			String substring(int start) {
				return this->substr(start);
			}
			
			String substring(int start, int end) {
				return this->substr(start, end - start);
			}
			
			String& toLowerCase() {
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
			
			String& toUpperCase() {
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