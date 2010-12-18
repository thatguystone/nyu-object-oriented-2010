/**
 * Following the java standard for concat -- make a copy of the string
 * if adding anything.
 */
String __String::{overload:concat[java.lang.String]}(String __this, String str) {
	if (str->__vptr->length(str) == 0) {
		return __this;
	} else {
		__String* ret = new __String(__this->__data);
		ret->__data.append(str->__data);
		return (String)ret;
	}
}

int32_t __String::{overload:length[]}(String __this) {
	return (int)__this->__data.size();
}

bool __String::{overload:contains[java.lang.String]}(String __this, String s) {
	return ((int)__this->__data.find(s->__data)) > -1;
}

bool __String::{overload:endsWith[java.lang.String]}(String __this, String s) {
	return (__this->__vptr->{overload:length[]}(__this) - s->__vptr->length(s)) == __this->__vptr->{overload:lastIndexOf[java.lang.String]}(__this, s);
}

int __String::{overload:indexOf[char]}(String __this, char_t ch) {
	return ((int)__this->__data.find(ch));
}

int __String::{overload:indexOf[java.lang.String]}(String __this, String str) {
	return ((int)__this->__data.find(str->__data));
}

int __String::{overload:lastIndexOf[char]}(String __this, char_t ch) {
	return ((int)__this->__data.rfind(ch));
}

int __String::{overload:lastIndexOf[java.lang.String]}(String __this, String str) {
	return ((int)__this->__data.rfind(str->__data));
}

String __String::{overload:replace[char,char]}(String __this, char_t old, char_t newChar) {
	String ret = new __String();
	
	int l = __this->__vptr->{overload:length[]}(__this);
	for (int i = 0; i < l; i++) {
		if (__this->__data.at(i) == old)
			ret->__data.push_back(newChar);
		else
			ret->__data.push_back(__this->__data.at(i));
	}
	
	return ret;
}

bool __String::{overload:startsWith[java.lang.String]}(String __this, String s) {
	return __this->__vptr->{overload:indexOf[java.lang.String]}(__this, s) == 0;
}

String __String::{overload:substring[int]}(String __this, int32_t start) {
	return (String)new __String(__this->__data.substr(start));
}

String __String::{overload:substring[int,int]}(String __this, int32_t start, int32_t end) {
	return (String)new __String(__this->__data.substr(start, end - start));
}

String __String::{overload:toLowerCase[]}(String __this) {
	String ret = new __String();
	
	int l = __this->__vptr->{overload:length[]}(__this);
	for (int i = 0; i < l; i++) {
		char chr = __this->__data.at(i);
		if (chr >= 'A' && chr <= 'Z')
			ret->__data.push_back(chr + ' ');
		else
			ret->__data.push_back(chr);
	}
	
	return ret;
}

String __String::{overload:toUpperCase[]}(String __this) {
	String ret = new __String();
	
	int l = __this->__vptr->{overload:length[]}(__this);
	for (int i = 0; i < l; i++) {
		char chr = __this->__data.at(i);
		if (chr >= 'a' && chr <= 'z')
			ret->__data.push_back(chr - ' ');
		else
			ret->__data.push_back(chr);
	}
	
	return ret;
}

String __String::{overload:toString[]}(String __this) {
	return __this;
}
