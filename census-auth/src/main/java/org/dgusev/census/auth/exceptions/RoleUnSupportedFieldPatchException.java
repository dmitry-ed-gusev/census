package org.dgusev.census.auth.exceptions;

import java.util.Set;

/***/

public class RoleUnSupportedFieldPatchException extends RuntimeException {

    public RoleUnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }
}
