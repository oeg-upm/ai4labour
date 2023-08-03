#!/bin/bash
echo "Content-type: text/html"
echo
echo "<html><body>wait 5 seconds</body></html>"
sudo systemctl stop apache2
cd /var/www/html
sudo rm -rf *
sudo rm -rf .git
sudo git clone "https://${GITHUB_TOKEN}@github.com/oeg-upm/ai4labour.git" .
sudo systemctl start apache2
echo "<html><body>wait x seconds</body></html>"
