include Makebase

.PHONY: src test doc

default: run

doc:
	javadoc -d doc \
		-windowtitle "Translator Docs" \
		-sourcepath $(SRCPATH) \
		-classpath $(XTCCLASSPATH) \
		-private \
		-use \
		-version \
		-splitIndex \
		translator translator.Printer translator.Expressions translator.Statements

src:
	$(MAKE) -C src
	
run: src
	$(MAKE) -C test run

test: src
	$(MAKE) -C test run args="-outputFile $(TRANSROOT)/out.cpp" > /dev/null 2>&1
	@echo "Running the Java:\n"
	@javac -sourcepath $(TESTPATH) test/$(file).java
	java -classpath $(TESTPATH) $(file)
	#sleep 3
	@echo "\n\n\n\nRunning the C++:\n"
	g++ $(TRANSROOT)/out.h $(TRANSROOT)/out.cpp
	./a.out

clean:
	$(MAKE) -C test clean
	$(MAKE) -C src clean
	rm -f *.cpp
	rm -f *.h
	rm -f *.out
	rm -f *.h.gch

help: src
	java -classpath $(RUNCLASSPATH) translator.Translator
