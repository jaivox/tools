
03:30 PM 09/02/2013

The classpath should be set to contain the path to the stanford nlp,
thus something like somepath/stanford/* where /somepath/stanford contains
all the jar files like stanford-parser-3.2.0-javadoc.jar

The wordnet database is wordnet30. The code assumes user of the database is
root with no password.

To compile: javac *.java
To run: java generate road.tree

First time it runs, it builds a file synonyms.txt which takes some time.
From then onwards, it loads that file instead of searching wordnet, and that
goes much faster.

