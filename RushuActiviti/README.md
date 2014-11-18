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

###ActiveMQ:
http://activemq.apache.org/getting-started.html

MQTT port: 1884
Storm port: 5673

####ActiveMQ start:

/usr/share/apache-activemq-5.9.1/bin/activemq console

###ActiveMQ stop:

/usr/share/apache-activemq-5.9.1/bin/activemqadmin stop


How to configure Activiti-Explorer and Activiti-Rest with OpenLDAP server.
=======================

reference:http://activiti.org/userguide/index.html#lda-explorer

Add the LDAP configuration as following to activiti-standalone-context.xml

    <bean id="processEngineConfiguration" class="...SomeProcessEngineConfigurationClass">
        ...
        <property name="configurators">
          <list>
              <bean class="org.activiti.ldap.LDAPConfigurator">
              
                <!-- Server connection params -->
                <property name="server" value="ldap://localhost" />
                <property name="port" value="33389" />
                <property name="user" value="uid=admin, ou=users, o=activiti" />
                <property name="password" value="pass" />
                
                <!-- Query params -->
                <property name="baseDn" value="o=activiti" />
                <property name="queryUserByUserId" value="(&(objectClass=inetOrgPerson)(uid={0}))" />
                <property name="queryUserByFullNameLike" value="(&(objectClass=inetOrgPerson)(|({0}=*{1}*)({2}=*{3}*)))" />
                <property name="queryGroupsForUser" value="(&(objectClass=groupOfUniqueNames)(uniqueMember={0}))" />
                
                <!-- Attribute config -->
                <property name="userIdAttribute" value="uid" />
                <property name="userFirstNameAttribute" value="cn" />
                <property name="userLastNameAttribute" value="sn" />
                <property name="userEmailAttribute" value="mail" />
                
                
                <property name="groupIdAttribute" value="cn" />
                <property name="groupNameAttribute" value="cn" />
                
              </bean>
          </list>
        </property>
</bean>         

Add the activiti-ldap jar to WEB-INF/lib

Remove the demoDataGenerator bean, as it will try to insert users (which is not possible with the LDAP integration)

Add following configuration to the explorerApp bean in activiti-ui.context:


    <property name="adminGroups">
      <list>
        <value>admin</value>
      </list>
    </property>
    <property name="userGroups">
	    <list>
          <value>users</value>
        </list>
    </property>
