include ../Makebase

#the name of the file to run
ifdef file
	FILE=$(file).java
	CFILE=$(file).class #the name of the class file for the run dependency
endif

#args to be passed to the run command
ARGS=
ifdef args
	ARGS=$(args)
endif

%.class: %.java
	javac -source 1.5 -classpath $(CLASSPATH) -sourcepath $(SOURCEPATH) $<

run: $(CFILE)
	java -classpath $(CLASSPATH) translator.Translator $(ARGS) $(FILE)

clean:
	find -name "*.class" -exec rm {} \;
