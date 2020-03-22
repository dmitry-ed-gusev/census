package org.dgusev.census.auth.api;

import org.dgusev.census.auth.domain.AuthRole;
import org.dgusev.census.auth.domain.AuthUser;
import org.dgusev.census.auth.repository.AuthRoleRepository;
import org.dgusev.census.auth.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***/

@RestController
@RequestMapping(path="/census/auth", produces="application/json")
@CrossOrigin(origins="*")
public class AuthRestController {

    private AuthUserRepository authUserRepository;
    private AuthRoleRepository authRoleRepository;

    public AuthRestController(AuthUserRepository authUserRepository, AuthRoleRepository authRoleRepository) {
        this.authUserRepository = authUserRepository;
        this.authRoleRepository = authRoleRepository;
    }

    @GetMapping("/users")
    public Iterable<AuthUser> listUsers() {
        return authUserRepository.findAll();
    }

    @GetMapping("/roles")
    public Iterable<AuthRole> listRoles() {
        return authRoleRepository.findAll();
    }

}
