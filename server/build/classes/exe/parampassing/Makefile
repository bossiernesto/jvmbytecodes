SERVERCODE = /home/naps/jhave_testing/Server/GaigsServer2000
CLIENTCODE = /home/naps/jhave_testing/client
### The shared directory/package is located here
SHAREDCODE = /home/naps/jhave_testing

test:
	export CLASSPATH=$(CLIENTCODE):$(SERVERCODE):$(SHAREDCODE):.; \
	printf "****** Making  the test\n"; \
	javac Test.java
# 	java Test

ranprobc:
	export CLASSPATH=$(CLIENTCODE):$(SERVERCODE):$(SHAREDCODE):.; \
	printf "****** Making  the problem generator\n"; \
	javac RandomProblemGenerator.java

ranprobr:
	export CLASSPATH=$(CLIENTCODE):$(SERVERCODE):$(SHAREDCODE):.; \
	printf "****** Making  the problem generator\n"; \
	javac RandomProblemGenerator.java; \
	cd ../..; \
 	java exe/parampassing/RandomProblemGenerator /home/goweyj98/Project1a/csf11/Server/GaigsServer2000/exe/parampassing/alg_vis.sho /home/goweyj98/Project1a/csf11/Server/GaigsServer2000/exe/parampassing/alg_vis2.sho /home/goweyj98/Project1a/csf11/Server/GaigsServer2000/exe/parampassing/alg_vis.html 1 1 1




