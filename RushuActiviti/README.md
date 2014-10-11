How to configure Activiti-Explorer and Activiti-Rest with same MySQL database.
=======================

###Reference:

http://www.attuneuniversity.com/blog/change-database-from-h2-to-mysql-in-activiti.html

###Local steps:

###Scp files:

####JDBC Driver:

scp mysql-connector-java-5.1.18-bin.jar root@www.rushucloud.com:/usr/share/tomcat6/webapps/activiti-explorer/WEB-INF/lib/

scp mysql-connector-java-5.1.18-bin.jar root@www.rushucloud.com:/usr/share/tomcat6/webapps/activiti-rest/WEB-INF/lib/

####DB properties:

scp db.properties root@www.rushucloud.com:/usr/share/tomcat6/webapps/activiti-explorer/WEB-INF/classes/

scp db.properties root@www.rushucloud.com:/usr/share/tomcat6/webapps/activiti-rest/WEB-INF/classes/

####Restart tomcat:

/usr/share/tomcat6/bin/shutdown.sh

/usr/share/tomcat6/bin/startup.sh
