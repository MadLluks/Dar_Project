#!/usr/bin/env bash

#
# Installs dependencies 
#

sudo apt-get install ruby1.9.3 git coffeescript
# Try to source nvm 
source ~/.nvm/nvm.sh &> /dev/null
# Check if nvm exists
nvm &> /dev/null
# If not, download it
if [ `echo $?` -ne "0" ]
then
	echo "Downloading nvm..."
	git clone https://github.com/creationix/nvm.git ~/.nvm && cd ~/.nvm && git checkout `git describe --abbrev=0 --tags` && source nvm.sh
fi
sudo nvm install v0.11.13 
nvm run v0.11.13
sudo npm install -g bower grunt-cli
sudo gem install foundation
sudo gem install compass

