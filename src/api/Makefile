include ../../Makebase

default: api

%.class: %.java
	javac -source 1.5 -classpath $(XTCCLASSPATH) -sourcepath $(JAVAAPIPATH) $<
 
api:
	$(MAKE) `../../make.srcs .`

clean:
	find -name "*.class" -exec rm {} \;
	find -name "*.o" -exec rm {} \;
	find -name "*.gch" -exec rm {} \;
	find -name "a.out" -exec rm {} \;
