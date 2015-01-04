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

Notice:

###1.mysql-grant-all-privileges-on-database:

GRANT ALL PRIVILEGES ON * . * TO 'root'@'localhost'

Profile:

###Test:

####OpenLDAP:http://www.server-world.info/en/note?os=Debian_7.0&p=ldap

sudo apt-get install slapd ldap-utils libldap-2.4-2 libdb4.6

####CentOS:http://docs.adaptivecomputing.com/viewpoint/hpc/Content/topics/1-setup/installSetup/settingUpOpenLDAPOnCentos6.htm

yum -y install openldap openldap-clients openldap-servers

####Docker:

At project/pom.xml,jar build: 

mvn clean install -DskipTests=true -Dspring.profiles.active=dev

At project/target/,docker build:

docker build -t eip-rushucloud-dev .

Deploy to Docker Container and run:

docker run -p 8082:8082 eip-rushucloud-dev