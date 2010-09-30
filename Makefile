include Makebase

.PHONY: src test

default: run

src:
	make -C src
	
test:
	make -C test

run: src
	@echo "\n" #make some room from the compilation messages to the program output
	@make -s -C test run #supress the annoying "leaving directory" message after the run completes
	
clean:
	make -C src clean
	make -C test clean
