19.03.2015
Server (Tomcat) setup for use simple security mechanism: you have to add thic realm config into Tomcat
server config [CATALINA_HOME\conf\server.xml]

 <Realm className="org.apache.catalina.realm.JDBCRealm" driverName="com.mysql.jdbc.Driver"
        connectionURL="jdbc:mysql://<hostname>:<port>/<dbname>?autoReconnect=true&amp;autoReconnectForPools=true&amp;maxReconnects=10&amp;user=<user>&amp;password=<pass>"
        userTable="users" userNameCol="user_name" userCredCol="user_pass" userRoleTable="user_roles" roleNameCol="role_name"/>

https://www.bogotobogo.com/DevOps/Docker/Docker_Apache_Tomcat.php
https://www.softwareyoga.com/docker-tomact-tutorial/
https://morioh.com/a/e92d6af60dfc/deploy-a-tomcat-application-using-docker-compose
https://www.devopstricks.in/deploy-tomcat-servlet-container-with-docker-compose/
https://github.com/Unidata/tomcat-docker/blob/latest/docker-compose.yml
https://devops4solutions.com/deploy-a-tomcat-application-using-docker-compose/
https://itecnote.com/tecnote/docker-tomcat-editing-configuration-files-through-dockerfile/
https://hub.docker.com/_/tomcat/tags?page=1&ordering=-last_updated
