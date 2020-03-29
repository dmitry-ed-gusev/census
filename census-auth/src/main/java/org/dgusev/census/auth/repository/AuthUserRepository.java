package org.dgusev.census.auth.repository;

import org.dgusev.census.auth.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

/***/

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
}
