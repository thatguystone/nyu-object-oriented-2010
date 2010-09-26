#the name of the file to run
FILE=add

default: translator

%.class: %.java
	javac -source 1.5 -classpath $(XTCROOT)/classes $<
	
translator:
	@cd $(XTCROOT); . ./setup.sh; make > /dev/null #make sure we have xtc all running and setup
	make `./make.srcs src`

run: translator test/$(FILE).class
	@echo "\n\n\n" #make some room from the compilation messages to the program output
	@java src.Translator test/$(FILE).java

clean-src:
	find src -name "*.class" -exec rm {} \;

clean: clean-src
