19.03.2015
Server (Tomcat) setup for use simple security mechanism: you have to add thic realm config into Tomcat
server config [CATALINA_HOME\conf\server.xml]

 <Realm className="org.apache.catalina.realm.JDBCRealm" driverName="com.mysql.jdbc.Driver"
        connectionURL="jdbc:mysql://<hostname>:<port>/<dbname>?autoReconnect=true&amp;autoReconnectForPools=true&amp;maxReconnects=10&amp;user=<user>&amp;password=<pass>"
        userTable="users" userNameCol="user_name" userCredCol="user_pass" userRoleTable="user_roles" roleNameCol="role_name"/>
