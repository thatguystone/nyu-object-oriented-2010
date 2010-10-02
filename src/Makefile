include ../Makebase

.PHONY: translator java

default: translator java

clean-translator:
	$(MAKE) -C translator clean

clean-java:
	$(MAKE) -C java clean

clean: clean-translator clean-java

#java has to be made from the parent directory, otherwise it fails (packages break)
#  * cd-ing from the child directory (java) to here in the makefile causes an error because none of java's rules are in this file
#  * the simplest solution was to move everything here
%.class: %.java
	javac -source 1.5 -classpath $(XTCCLASSPATH) -sourcepath $(SRCPATH) $<

java:
	$(MAKE) DIRECTORY=java `../make.srcs java`
	
#for the same reason as java, translator was moved here
translator:
	$(MAKE) DIRECTORY=translator `../make.srcs translator`
