include ../Makebase

#the name of the file to run
ifdef file
	FILE=$(file).java
endif

#args to be passed to the run command
ARGS=
ifdef args
	ARGS=$(args)
endif

%.class: %.java
	javac -source 1.5 -classpath $(RUNCLASSPATH) -sourcepath $(TESTPATH) $<

allTests:
	$(MAKE) `../make.srcs .`

run: allTests
	#run the translator
	java \
		-classpath $(RUNCLASSPATH) \
		translator.Translator $(ARGS) \
		-in $(TESTPATH) -in $(SRCPATH) \
		$(FILE)

clean:
	find -name "*.class" -exec rm {} \;
