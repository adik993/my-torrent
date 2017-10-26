#!/bin/bash

install_dir=`pwd`

sudo systemctl stop my-torrent.service
sudo systemctl disable my-torrent.service

cd ..
echo "Removing $install_dir"
sudo rm -r ${install_dir}

sudo userdel mytorrent