package org.dgusev.census.auth.repository;

import org.dgusev.census.auth.domain.entity.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;

/***/

public interface AuthRoleRepository extends JpaRepository<AuthRole, Long> {
}
