PACKAGE     = translator
PACKAGE_LOC = translator

SOURCE = \
	Bob.java

default: translator

translator:
	@cd $(XTCROOT); . ./setup.sh; make > /dev/null #make sure we have xtc all running and setup
	javac -source 1.5 -classpath $(XTCROOT)/classes *.java
	@echo "All compiled!"

run: translator
	@echo "\n\n\n" #make some room from the compilation messages to the program output
	@java Translator

clean:
	rm *.class
