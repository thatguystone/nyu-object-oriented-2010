/**
 * Following the java standard for concat -- make a copy of the string
 * if adding anything.
 */
String __String::{overload:concat[java.lang.String]}(String __this, String str) {
	if (str->__vptr->length() == 0) {
		return *this;
	} else {
		String *ret = new String();
		*ret = *__this;
		ret->append(str);
		return *ret;
	}
}

int32_t __String::{overload:length[]}() {
	return (int)this->__vptr->size();
}

bool __String::{overload:contains[java.lang.String]}(String __this, String s) {
	return ((int)this->find(s)) > -1;
}

bool __String::{overload:endsWith[java.lang.String]}(String __this, String s) {
	return (this->length() - s.length()) == this->lastIndexOf(s);
}

int __String::{overload:indexOf[char]}(String __this, char ch) {
	return ((int)this->find(ch));
}

int __String::{overload:indexOf[java.lang.String]}(String __this, String str) {
	return ((int)this->find(str));
}

int __String::{overload:lastIndexOf[char]}(String __this, char ch) {
	return ((int)this->rfind(ch));
}

int __String::{overload:lastIndexOf[java.lang.String]}(String __this, String str) {
	return ((int)this->rfind(str));
}

String __String::{overload:replace[char,char]}(String __this, char old, char newChar) {
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

bool __String::{overload:startsWith[java.lang.String]}(String __this, String s) {
	return this->indexOf(s) == 0;
}

String __String::{overload:substring[int]}(String __this, int32_t start) {
	return this->substr(start);
}

String __String::{overload:substring[int]}(String __this, int32_t start, int32_t end) {
	return this->substr(start, end - start);
}

String __String::{overload:toLowerCase[]}(String __this) {
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

String __String::{overload:toUpperCase[]}(String __this) {
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
