include ../Makebase

.PHONY: translator java xtc

default: translator java specialCases xtc

#java has to be made from the parent directory, otherwise it fails (packages break)
#  * cd-ing from the child directory (java) to here in the makefile causes an error because none of java's rules are in this file
#  * the simplest solution was to move everything here
%.class: %.java
	javac -source 1.5 -classpath $(XTCCLASSPATH) -sourcepath $(SRCPATH) $<

java:
	$(MAKE) -C api
	
#for the same reason as java, translator was moved here
translator:
	$(MAKE) `../make.srcs translator`
	
specialCases:
	$(MAKE) `../make.srcs SpecialCases`

#for our xtc base
xtc:
	cd $(XTCROOT); . ./setup.sh; $(MAKE) classes

clean-api:
	$(MAKE) -C api clean

clean-translator:
	find translator -name "*.class" -exec rm {} \;
	
clean-specialCases:
	find SpecialCases -name "*.class" -exec rm {} \;

clean: clean-api clean-translator clean-specialCases
