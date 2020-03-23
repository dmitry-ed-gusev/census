package org.dgusev.census.auth.repository;

import org.dgusev.census.auth.domain.AuthUser;
import org.springframework.data.repository.CrudRepository;

/***/

public interface AuthUserRepository extends CrudRepository<AuthUser, Long> {
}
