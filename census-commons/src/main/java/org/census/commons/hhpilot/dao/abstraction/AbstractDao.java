package org.census.commons.hhpilot.dao.abstraction;


import org.census.commons.hhpilot.connection.interfaces.ConnectionManagerInterface;

import java.sql.Connection;

/**
 * Abstract Dao component. All other Dao components should extend this one.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 26.11.12)
 */

public abstract class AbstractDao {

    // link to connection manager
    private ConnectionManagerInterface connectionManager;

    public AbstractDao(ConnectionManagerInterface connectionManager) {
        if (connectionManager == null) {
            throw new IllegalStateException("Connection manager is null!");
        } else {
            this.connectionManager = connectionManager;
        }
    }

    public Connection getConnection() {
        return this.connectionManager.getConnection();
    }

}
