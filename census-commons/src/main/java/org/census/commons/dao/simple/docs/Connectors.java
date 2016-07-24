package org.census.commons.dao.simple.docs;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/***/
public class Connectors {
    private static DataSource personnelDS = null;
    private static DataSource usersDS = null;
    private static DataSource memoDS = null;

    public Logger logger = Logger.getLogger(getClass().getName());

    public Connectors() {
        if ((personnelDS == null) && (usersDS == null) && (memoDS == null)) {
            // Непосредственная инициализация контекста JNDI и поиск необходимого ресурса по имени
            Context context = null;
            try {
                context = new InitialContext();
                personnelDS = (DataSource) context.lookup(Defaults.PERSONNEL_SOURCE);
                logger.info("Data source [" + Defaults.PERSONNEL_SOURCE + "] initialized!");

                usersDS = (DataSource) context.lookup(Defaults.USERS_SOURCE);
                logger.info("Data source [" + Defaults.USERS_SOURCE + "] initialized!");

                memoDS = (DataSource) context.lookup(Defaults.MEMO_SOURCE);
                logger.info("Data source [" + Defaults.MEMO_SOURCE + "] initialized!");

            }
            // Перехват ИС
            catch (NamingException e) {
                logger.error("Can't initialize: " + e.getMessage());
            }
            // Закрываем контекст. Хотя, если его и не закрывать, то разницы особой нет (ну типа экономим ресурсы).
            finally {
                try {
                    if (context != null) {
                        context.close();
                    }
                }
                catch (NamingException e) {
                    logger.error("Can't close context! Reason: " + e.getMessage());
                }
            }
        } else {
            logger.debug("One of datasourses already initialized!");
        }
    }

    public Connection getPersonnelConnection() throws SQLException {
        Connection conn;
        if ((personnelDS != null) && (usersDS != null) && (memoDS != null)) {
            conn = personnelDS.getConnection();
            if (conn == null) {
                throw new SQLException("Connection for personnel DS is null!");
            }
        } else {
            throw new SQLException("One of data sources not initilized!");
        }
        return conn;
    }

    public Connection getUsersConnection() throws SQLException {
        Connection conn;
        if ((personnelDS != null) && (usersDS != null) && (memoDS != null)) {
            conn = usersDS.getConnection();                        
            if (conn == null) {
                throw new SQLException("Connection for users DS is null!");
            }
        } else {
            throw new SQLException("One of data sources not initilized!");
        }
        return conn;
    }

    public Connection getMemoConnection() throws SQLException {
        Connection conn;
        if ((personnelDS != null) && (usersDS != null) && (memoDS != null)) {
            conn = memoDS.getConnection();
            if (conn == null) {
                throw new SQLException("Connection for memo DS is null!");
            }
        } else {
            throw new SQLException("One of data sources not initilized!");
        }
        return conn;
    }

}
