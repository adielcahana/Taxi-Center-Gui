all: compile jar

bin:
	mkdir bin

compile: bin
	javac -d bin src/*.java

jar:
	jar cfm gui.jar Manifest.txt -C bin . -C resources . 

