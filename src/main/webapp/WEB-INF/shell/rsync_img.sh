#!/bin/sh
date >> /home/webuser/akb/log/crontab/rsync_img.log
rsync -av /home/webuser/akb/pfp/img/ kdpic1.mypchome.com.tw::img/
rsync -av /home/webuser/akb/pfp/img/ kdpic2.mypchome.com.tw::img/
rsync -av /home/webuser/akb/pfp/img/ kdpic3.mypchome.com.tw::img/
rsync -av /home/webuser/akb/pfp/img/ kdpic4.mypchome.com.tw::img/
rsync -av /home/webuser/akb/pfp/img/ kdpic5.mypchome.com.tw::img/
