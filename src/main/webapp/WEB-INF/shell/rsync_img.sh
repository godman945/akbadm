#!/bin/sh
date >> /home/webuser/akb/log/crontab/rsync_img.log
rsync -av /home/webuser/akb/pfp/img/ 172.21.10.11::img/
rsync -av /home/webuser/akb/pfp/img/ 172.21.10.12::img/
rsync -av /home/webuser/akb/pfp/img/ 172.21.10.13::img/
rsync -av /home/webuser/akb/pfp/img/ 172.21.10.14::img/
rsync -av /home/webuser/akb/pfp/img/ 172.21.10.15::img/
