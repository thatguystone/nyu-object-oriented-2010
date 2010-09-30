#the classpath
CLASSPATH=$(XTCROOT)/classes:.

#the name of the file to run
ifdef file
	FILE=test/$(file).java
	CFILE=test/$(file).class #the name of the class file for the run dependency
endif

#args to be passed to the run command
ARGS=
ifdef args
	ARGS=$(args)
endif

default: translator

%.class: %.java
	javac -source 1.5 -classpath $(CLASSPATH) $<
	
translator:
	@cd $(XTCROOT); . ./setup.sh; make > /dev/null #make sure we have xtc all running and setup
	make `./make.srcs src`

run: translator $(CFILE)
	@echo "\n" #make some room from the compilation messages to the program output
	java -classpath $(CLASSPATH) src.Translator $(ARGS) $(FILE)

clean-src:
	find src -name "*.class" -exec rm {} \;

clean-test:
	find test -name "*.class" -exec rm {} \;

clean: clean-src clean-test
