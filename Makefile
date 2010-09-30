include Makebase

.PHONY: src test

default: run

src:
	make -C src
	
test:
	make -C test

run: src
	make -C test run
	
clean:
	make -C src clean
	make -C test clean
