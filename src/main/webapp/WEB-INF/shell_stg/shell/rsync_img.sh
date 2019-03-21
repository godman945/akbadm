#!/bin/sh
date >> /home/webuser/akb/log/crontab/rsync_img.log
rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kwstg2.pchome.com.tw:/home/webuser/akb/pfp/img/
#rsync -avp -e ssh /home/webuser/akb/pfp/img/ webuser@kdpic2.mypchome.com.tw:/home/webuser/akb/adpic/img/

#rsync -avP /home/webuser/akb/pfp/img kdpic1.mypchome.com.tw::img/
#rsync -avP /home/webuser/akb/pfp/img kdpic2.mypchome.com.tw::img/
