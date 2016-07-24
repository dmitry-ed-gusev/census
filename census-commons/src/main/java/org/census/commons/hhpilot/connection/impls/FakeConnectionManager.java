package org.census.commons.hhpilot.connection.impls;


import org.census.commons.hhpilot.connection.interfaces.ConnectionManagerInterface;

import java.sql.Connection;

/**
 * We need this implementation only for first time.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 26.11.12)
*/

public class FakeConnectionManager implements ConnectionManagerInterface {

    @Override
    public Connection getConnection() {
        return null;
    }

}