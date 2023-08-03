#!/bin/bash
sudo systemctl stop apache2
cd /var/www/html
sudo rm -rf *
sudo rm -rf .git
direccion=http://"$GITHUB_TOKEN"@github.com/oeg-upm/ai4labour.git
sudo git clone $direccion .
sudo systemctl start apache2
