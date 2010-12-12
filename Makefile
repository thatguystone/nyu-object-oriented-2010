include Makebase

.PHONY: src test doc

default: run

%.h.gch: %.h
	g++ $< > /dev/null 2>&1
	
doc:
	javadoc -d doc \
		-windowtitle "Translator Docs" \
		-sourcepath $(SRCPATH) \
		-classpath $(XTCCLASSPATH) \
		-private \
		-use \
		-version \
		-splitIndex \
		translator translator.Printer translator.Expressions translator.Statements translator.SpecialCases

src:
	$(MAKE) -C src
	
run: src
	$(MAKE) -C test run

cpp_run: $(TRANSROOT)/out.h.gch
	g++ $(TRANSROOT)/out.cpp
	./a.out

cpp: run cpp_run

test: run src
	$(MAKE) -C test run > /dev/null 2>&1
	@echo "Running the Java:\n"
	@javac -sourcepath $(TESTPATH) test/$(file).java
	java -classpath $(TESTPATH) $(file)
	
	@echo "\n\n\n\nRunning the C++:\n"
	make cpp_run

clean:
	$(MAKE) -C test clean
	$(MAKE) -C src clean
	rm -f *.cpp
	rm -f *.h
	rm -f *.out
	rm -f *.h.gch

help: src
	java -classpath $(RUNCLASSPATH) translator.Translator
