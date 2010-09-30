include Makebase

.PHONY: src test

default: run

src:
	make -C src
	
test:
	make -C test

run: src
	@make -C test run #supress the annoying "leaving directory" message after the run completes
	
clean:
	make -C src clean
	make -C test clean
