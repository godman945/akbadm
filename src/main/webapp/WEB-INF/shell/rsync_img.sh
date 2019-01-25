#!/bin/sh
date >> /home/webuser/akb/log/crontab/rsync_img.log
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic1.mypchome.com.tw:/home/webuser/akb/adpic/img/
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic2.mypchome.com.tw:/home/webuser/akb/adpic/img/
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic3.mypchome.com.tw:/home/webuser/akb/adpic/img/
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic4.mypchome.com.tw:/home/webuser/akb/adpic/img/
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic5.mypchome.com.tw:/home/webuser/akb/adpic/img/
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic6.mypchome.com.tw:/home/webuser/akb/adpic/img/
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic7.mypchome.com.tw:/home/webuser/akb/adpic/img/
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic8.mypchome.com.tw:/home/webuser/akb/adpic/img/
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic9.mypchome.com.tw:/home/webuser/akb/adpic/img/
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic10.mypchome.com.tw:/home/webuser/akb/adpic/img/
#rsync -avP /home/webuser/akb/pfp/img kdpic1.mypchome.com.tw::img/
#rsync -avP /home/webuser/akb/pfp/img kdpic2.mypchome.com.tw::img/
