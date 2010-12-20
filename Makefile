include Makebase

.PHONY: src test doc

default: run

TEST_ARGS="arg1" "arg2" "arg3"

doc:
	javadoc -d doc \
		-windowtitle "Translator Docs" \
		-sourcepath $(SRCPATH) \
		-classpath $(XTCCLASSPATH) \
		-private \
		-use \
		-version \
		-splitIndex \
		translator translator.Printer translator.Expressions translator.Statements SpecialCases

src:
	$(MAKE) -C src
	
run: src
	$(MAKE) -C test run

cpp_compile:
	g++ $(OUTPUTHEADER) > /dev/null 2>&1
	g++ -o $(OUTPUTBINARY) $(OUTPUTFILE)

cpp_run: cpp_compile
	$(OUTPUTBINARY) $(TEST_ARGS)

java_run:
	@javac -sourcepath $(TESTPATH) test/$(file).java
	java -classpath $(TESTPATH) $(file) $(TEST_ARGS)

cpp: run cpp_run

test: run src
	$(MAKE) -C test run > /dev/null 2>&1
	@echo "Running the Java:\n"
	$(MAKE) java_run
	
	@echo "\n\n\n\nRunning the C++:\n"
	$(MAKE) cpp_run

clean:
	$(MAKE) -C test clean
	$(MAKE) -C src clean
	rm -f *.cpp
	rm -f *.h
	rm -f *.out
	rm -f *.h.gch

help: src
	java -classpath $(RUNCLASSPATH) translator.Translator
	
testSuite: src
	$(MAKE) -C test testSuite

compileTestFile:
	$(MAKE) -C test $(file)
