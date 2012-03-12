#!/bin/bash

cd src #cd into src directory

find -name "*.java" | while read file; do
	#find every .java file

	#count non-empty lines
	sed '/^$/d' "$file" | wc -l

done | awk '{total+=$0}END{print "Total lines of code:", total}'
#let awk calculate total and print it
