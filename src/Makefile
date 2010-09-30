include ../Makebase

.PHONY: translator java

default: translator java

%.class: %.java
	javac -source 1.5 -classpath $(CLASSPATH) $<
	
translator:
	@cd $(XTCROOT); . ./setup.sh; make > /dev/null #make sure we have xtc all running and setup
	make `../make.srcs translator`

java:
	make `../make.srcs java`

clean-translator:
	find translator -name "*.class" -exec rm {} \;

clean-java:
	find java -name "*.class" -exec rm {} \;

clean: clean-translator clean-java
