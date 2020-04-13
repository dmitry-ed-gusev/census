package org.dgusev.census.auth;

/** Census Auth Service defaults. Some of them can be moved to application.properties file. */
public final class CensusAuthDefaults {

    /** Service PID file name. */
    public static final String PID_FILE_NAME = "census-auth-service.pid";
    /** Service web server port number. */
    public static final String PORT_FILE_NAME= "census-auth-service.port";

    /***/
    public static final String URI_DEFAULT       = "/census/api/auth";
    /***/
    public static final String URI_USERS         = "/users";
    /***/
    public static final String URI_USERS_WITH_ID = URI_USERS + "/{id}";
    /***/
    public static final String URI_ROLES         = "/roles";
    /***/
    public static final String URI_ROLES_WITH_ID = URI_ROLES + "/{id}";

    private CensusAuthDefaults() {}
}
