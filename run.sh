#!/bin/sh
#Los primeros con background, pero el ultimo no debe morir
#sudo python3 /nlp/wordfreq/wf.py&
#sudo python3 /nlp/semanticfields/sf.py&
sudo /usr/bin/java -jar /victor/target/crec-1.0.jar --server.port=8080 --server.use-forward-headers=true

