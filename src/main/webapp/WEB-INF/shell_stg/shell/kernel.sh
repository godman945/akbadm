cd /home/webuser/akb/webapps/adm/WEB-INF/classes
java -Dlog4j.configurationFile=config/log4j/log4j2.xml -cp .:../lib/*:/usr/share/tomcat-7/lib/* com.pchome.akbadm.quartzs.KernelJob $*
