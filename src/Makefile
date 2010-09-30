include ../Makebase

.PHONY: translator java

default: translator java

translator:
	make -C translator

clean-translator:
	make -C translator clean

clean-java:
	make -C java clean

clean: clean-translator clean-java

#java has to be made from the parent directory, otherwise it fails (packages break)
#  * cd-ing from the child directory (java) to here in the makefile causes an error because none of java's rules are in this file
#  * the simplest solution was to move everything here
%.class: %.java
	javac -source 1.5 -sourcepath java $<

java:
	make `../make.srcs java`
