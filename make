#!/bin/bash
if [ ! -e "./src/" ]; then
	echo "This must be run from the translator root directory. Resistance is futile."
else
	. config.sh
	make -s $*
fi
