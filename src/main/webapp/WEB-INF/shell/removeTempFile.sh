cd /home/webuser/akb/webapps/admDevelop/WEB-INF/classes

java -cp .:../lib/*:/usr/share/tomcat-6/lib/* com.pchome.akbadm.api.RemoveTempFileAPI $*
