RushuEIP overview:

###1.Java Spring-boot for EIP codebase;

###2.Activiti for workflow solution;

###3.ActiveMQ for message solution;

###4.LDAP for enterprise authentication;

###5.MySQL/JPA.

###6.Swagger UI for Restful API.

###7.Others.


References:

http://spring.io/guides

http://www.activiti.org/

https://activemq.apache.org

http://en.wikipedia.org/wiki/Lightweight_Directory_Access_Protocol


LDAP:

http://www.ibm.com/developerworks/cn/linux/l-openldap/

http://fanqiang.chinaunix.net/app/ldap/2005-03-23/2979.shtml

http://hermanbanken.nl/2011/01/22/openldap-server-mac-osx-clients/

Dynamic/JasperReport:

http://krams915.blogspot.in/2011/02/spring-3-dynamicjasper-hibernate_4736.html

MVN:

http://docs.spring.io/autorepo/docs/spring-boot/1.2.0.BUILD-SNAPSHOT/maven-plugin/examples/run-debug.html

Install Activiti-5.17 SNAPSHOT at local steps:

mvn clean install -DskipTests=true

1.Activiti/modules/activiti-simple-workflow for Activiti/modules/activiti-rest

2.Activiti/modules/activiti-common-rest,activiti-rest,activiti-spring for activiti-starter-basic

2.Activiti/modules/activiti-spring-boot

Notice:

###1.mysql-grant-all-privileges-on-database:

GRANT ALL PRIVILEGES ON * . * TO 'root'@'localhost'

Profile:

###Test:

mvn spring-boot:run -DskipTests=true -Dspring.profiles.active=dev

####OpenLDAP:

http://www.server-world.info/en/note?os=Debian_7.0&p=ldap

sudo apt-get install slapd ldap-utils libldap-2.4-2 libdb4.6

http://www.unixmen.com/openldap-installation-configuration-ubuntu-12-1013-0413-10-debian-67/

####CentOS:

http://docs.adaptivecomputing.com/viewpoint/hpc/Content/topics/1-setup/installSetup/settingUpOpenLDAPOnCentos6.htm

yum -y install openldap openldap-clients openldap-servers

####Package JAR then run:

mvn clean install -DskipTests=true -Dspring.profile.test=true

http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html

java -jar eip-rushucloud-0.0.1.jar --spring.profiles.active=test

####Docker:

At project/pom.xml,jar build: 

mvn clean install -DskipTests=true -Dspring.profiles.active=dev

At project/target/,docker build:

docker build -t eip-rushucloud-dev .

Deploy to Docker Container and run:

docker run -p 8082:8082 eip-rushucloud-dev

####JDK8:

#####Debain:

PPA + Oracle-JDK-8-installer: 

http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html

####RabbitMQ:

http://182.92.232.131:15672/

reim:WNhZDU2Yzg0M2RiN

http://ng.rushucloud.com:55672/ guest/guest

####Enviroment(123.56.112.163):

mysql:FiOWExZTM4ZGI0NmE 

database:reim_ng

grant all privileges on reim_ng.* to reim_ng@'localhost' identified by 'zZDVjMDkwNmU5MTA4OTJlO';

LDAP: 123.56.112.163:389 cn=admin,dc=123,dc=56,dc=112,dc=163 Rushu0915