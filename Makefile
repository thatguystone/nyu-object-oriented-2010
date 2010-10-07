include Makebase

.PHONY: src test

default: run

src:
	$(MAKE) -C src
	
test:
	$(MAKE) -C test

run: src
	$(MAKE) -C test run
	
clean:
	$(MAKE) -C src clean
	$(MAKE) -C test clean

help: src
	java -classpath $(RUNCLASSPATH) translator.Translator
