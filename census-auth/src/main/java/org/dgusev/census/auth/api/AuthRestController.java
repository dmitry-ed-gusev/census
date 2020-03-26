package org.dgusev.census.auth.api;

import org.dgusev.census.auth.domain.AuthRole;
import org.dgusev.census.auth.domain.AuthUser;
import org.dgusev.census.auth.repository.AuthRoleRepository;
import org.dgusev.census.auth.repository.AuthUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    // Find users with assigned roles
    @GetMapping("/users")
    public Iterable<AuthUser> listUsers() {
        return authUserRepository.findAll();
    }

    // Save user; return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    AuthUser newAuthUser(@RequestBody AuthUser newAuthUser) {
        return authUserRepository.save(newAuthUser);
    }

    // Find roles
    @GetMapping("/roles")
    public Iterable<AuthRole> listRoles() {
        return authRoleRepository.findAll();
    }

    // Save role; return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/roles")
    AuthRole newAuthRole(@RequestBody AuthRole newAuthRole) {
        return authRoleRepository.save(newAuthRole);
    }

}
