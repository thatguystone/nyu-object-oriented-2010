include ../../Makebase

default: api

%.class: %.java
	javac -source 1.5 -classpath $(XTCCLASSPATH) -sourcepath $(JAVAAPIPATH) -d $(APICLASSPATH) $<
 
api:
	$(MAKE) `../../make.srcs .`
