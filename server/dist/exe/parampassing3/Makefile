SERVERCODE = /home/goweyj98/Project/csf11/Server/GaigsServer2000
CLIENTCODE = /home/goweyj98/Project/csf11/client
### The shared directory/package is located here
SHAREDCODE = /home/goweyj98/Project/csf11

test:
	export CLASSPATH=$(CLIENTCODE):$(SERVERCODE):$(SHAREDCODE):.; \
	printf "****** Making  the test\n"; \
	javac Test.java; \
 	java Test

ranprob:
	export CLASSPATH=$(CLIENTCODE):$(SERVERCODE):$(SHAREDCODE):.; \
	printf "****** Making  the problem generator\n"; \
	javac RandomProblemGenerator.java; \
	cd ../..; \
	java exe/parampassing3/RandomProblemGenerator /home/goweyj98/Project/csf11/Server/GaigsServer2000/exe/parampassing3/alg_vis.sho /home/goweyj98/Project/csf11/Server/GaigsServer2000/exe/parampassing3/alg_vis2.sho /home/goweyj98/Project/csf11/Server/GaigsServer2000/exe/parampassing3/alg_vis.html 1 1



