package org.dgusev.census.auth.exceptions;

/***/

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(Long id ) {
        super("Role id not found : " + id);
    }
}
