!/bin/bash
sudo systemctl stop apache2
cd /var/www/html
sudo rm -rf *
sudo rm -rf .git
sudo git clone "https://${GITHUB_TOKEN}@github.com/oeg-upm/ai4labour.git" .
sudo systemctl start apache2