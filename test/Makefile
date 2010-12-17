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
	java \
		-classpath $(RUNCLASSPATH) \
		translator.Translator $(ARGS) \
		-outputFile $(OUTPUTFILE) \
		-in $(TESTPATH) -in $(JAVAAPIPATH) \
		$(FILE)

clean:
	find -name "*.class" -exec rm {} \;
	find -name "*.pyc" -exec rm {} \;
	find -name "*.h" -exec rm {} \;
	find -name "*.cpp" -exec rm {} \;
	find -name "*.out" -exec rm {} \;

testSuite: allTests
	python testSuite.py
