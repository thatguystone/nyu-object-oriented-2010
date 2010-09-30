%.class: %.java
	javac -source 1.5 -classpath $(XTCROOT)/classes $<
	
translator:
	@cd $(XTCROOT); . ./setup.sh; make > /dev/null #make sure we have xtc all running and setup
	make `../../make.srcs .`

clean:
	find -name "*.class" -exec rm {} \;
