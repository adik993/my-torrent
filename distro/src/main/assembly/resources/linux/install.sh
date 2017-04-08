#!/bin/bash

install_dir=$1

if [ -z "$install_dir" ]
then
    echo "Usage:"
    echo "$0 install_dir"
    exit 1
fi
install_dir=$install_dir/my-torrent
mkdir -p $install_dir
cp my-torrent.service $install_dir/
cp my-torrent.jar $install_dir/
cp uninstall.sh $install_dir/

sed -i "s|@install-dir@|$install_dir|g" $install_dir/my-torrent.service

id -u mytorrent &>/dev/null || sudo useradd mytorrent
sudo chown mytorrent:mytorrent $install_dir/*
sudo chown mytorrent:mytorrent $install_dir
sudo chmod 500 $install_dir/my-torrent.jar

sudo systemctl stop my-torrent.service
sudo systemctl disable my-torrent.service

sudo ln -s $install_dir/my-torrent.service /etc/systemd/system/my-torrent.service
sudo systemctl enable my-torrent.service
sudo systemctl start my-torrent.service
