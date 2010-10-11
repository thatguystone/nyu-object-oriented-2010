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
		translator

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
