all:
	@rm -rf od
	@mkdir od
	@javac src/io/github/triploit/*.java src/io/github/triploit/parser/*.java src/io/github/triploit/parser/token/*.java -d od
	@jar cvfm od/aml.jar src/META-INF/MANIFEST.MF -C od .