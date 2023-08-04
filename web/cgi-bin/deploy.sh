#!/bin/bash
echo "Content-type: text/html"
echo
echo "<html><body>wait 5 seconds</body></html>"
cd /victor
git pull
mvn clean install

