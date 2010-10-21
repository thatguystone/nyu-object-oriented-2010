/**
 * ClassParsers job is to generically find the correct JavaClass name
 * This is to avoid creating Template Specialization at runtime
 */ 

#pragma once

#include <iostream>
#include <vector> 
#include <typeinfo>
#include <stdint.h>
#include <string>
#include <cstring>


typedef std::string String; 
typedef std::vector<std::string> Vector;
class ClassParser;


class ClassParser {

	public: 
		ClassParser(){};
	
	/**
	 * Get the generic type in the form of some bizarre ID (of no use to us yet)
	 */
	template <typename T> 
	String getTypeID() { 
		T t;
	     	String type = typeid(T).name(); 
	     	return type;
	}

	/**
	 * Parse through the generic type ID and get what we need, a vector of the total package
	 */
	Vector tokenize_string(const std::string &original_string, const std::string &delimiters) {
		Vector tokens;
		size_t pos_start = original_string.find_first_not_of(delimiters);
	        size_t pos_end   = original_string.find_first_of(delimiters, pos_start);
		
		while (std::string::npos != pos_start) {
	                tokens.push_back(original_string.substr(pos_start, pos_end - pos_start));
	                pos_start = original_string.find_first_not_of(delimiters, pos_end);
	                pos_end   = original_string.find_first_of(delimiters, pos_start);
	        }
	        return tokens;
	}
	
	/**
	 * Concatenate the vectors contents into one final string we can pass around
	 */	
	template <typename T>
	String getJavaClassName() {
	 	int i;
	 	String classPath;
	 	String className;
	 	String type = getTypeID<T>();
	       	if (type.length() > 1) {
	       		classPath = "[L";
	       		i = 1;
	      	}
	       	else {
	       		classPath = "[";
	       		i = 0;
	       	}
	       	String delimitters = "123456789_";
	       	Vector tokens = tokenize_string(type, delimitters);
	        while(i < tokens.size()) {
	        	if (i == (tokens.size() - 1) && type.length() > 1) {
	        		className = tokens[i].substr(0, (tokens[i].length() - 1));
	        		classPath = classPath + className;
	        	}
	        	else if (i == (tokens.size() - 1))
	        		classPath = classPath + tokens[i];
	        	else 
	        		classPath = classPath + tokens[i] + ".";
	        	i++;
	        }
	       	return classPath;
	}
};
