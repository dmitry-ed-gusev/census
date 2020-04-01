package org.dgusev.census.auth.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dgusev.census.auth.domain.AuthRole;
import org.dgusev.census.auth.domain.AuthUser;
import org.dgusev.census.auth.exceptions.RoleNotFoundException;
import org.dgusev.census.auth.exceptions.RoleUnSupportedFieldPatchException;
import org.dgusev.census.auth.exceptions.UserNotFoundException;
import org.dgusev.census.auth.exceptions.UserUnSupportedFieldPatchException;
import org.dgusev.census.auth.repository.AuthRoleRepository;
import org.dgusev.census.auth.repository.AuthUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/***/

@Slf4j
@RestController // <- allows write response directly to response body
@RequestMapping(path = "/census/auth", produces = MediaType.APPLICATION_JSON_VALUE) // <- methods produce JSONs
@CrossOrigin(origins="*")
public class AuthRestController {

    private AuthUserRepository authUserRepository;
    private AuthRoleRepository authRoleRepository;

    public AuthRestController(AuthUserRepository authUserRepository, AuthRoleRepository authRoleRepository) {
        LOG.debug("AuthRestController constructor() is working.");
        this.authUserRepository = authUserRepository;
        this.authRoleRepository = authRoleRepository;
    }

    // Find all users with assigned roles
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK) // <- usually default status on success
    public Iterable<AuthUser> listUsers() {
        LOG.debug("AuthRestController.listUsers() is working.");
        return authUserRepository.findAll();
    }

    // Save user; return 201 instead of 200 (on success)
    @PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUser newAuthUser(@RequestBody AuthUser newAuthUser) {
        LOG.debug("AuthRestController.newAuthUser() is working.");

        return authUserRepository.save(newAuthUser);
    }

    // Find user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<AuthUser> findOneUser(@PathVariable Long id) {
        LOG.debug("AuthRestController.findOneUser() is working. User id = {}.", id);

        // option 1: throw exception if not found
        // return authUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        // option 2: return NOT_FOUND if not found
        Optional<AuthUser> authUser = authUserRepository.findById(id);
        return authUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

    }

    // Save or update user by id
    @PutMapping(path = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthUser saveOrUpdateAuthUser(@RequestBody AuthUser newAuthUser, @PathVariable Long id) {
        LOG.debug("AuthRestController.saveOrUpdateAuthUser() is working. User id = {}.", id);

        return authUserRepository.findById(id)
                .map(user -> {
                    LOG.debug("User id = {} found. Updating with provided values.", id);
                    user.setName(newAuthUser.getName());
                    user.setDescription(newAuthUser.getDescription());
                    user.setPassword(newAuthUser.getPassword());
                    user.setUsername(newAuthUser.getUsername());
                    return authUserRepository.save(user);
                })
                .orElseGet(() -> {
                    LOG.debug("User id = {} not found. Adding new user.", id);
                    newAuthUser.setId(id);
                    return authUserRepository.save(newAuthUser);
                });
    }

    // Update user name and description
    @PatchMapping(path = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthUser patchAuthUser(@RequestBody Map<String, String> update, @PathVariable Long id) {
        LOG.debug("AuthRestController.patchAuthUser() is working. User id = {}.", id);

        return authUserRepository.findById(id)
                .map(user -> {
                    String name = update.get("name");
                    String description = update.get("description");
                    if (!StringUtils.isEmpty(name) || !StringUtils.isEmpty(description)) { // there are fields for update
                        if (!StringUtils.isEmpty(name)) { // name is not empty - update it
                            user.setName(name);
                        }
                        if (!StringUtils.isEmpty(description)) { // description is not empty - update it
                            user.setDescription(description);
                        }
                        // better create a custom method to update a value = :newValue where id = :id
                        return authUserRepository.save(user);
                    } else {
                        throw new UserUnSupportedFieldPatchException(update.keySet());
                    } // end of IF
                }) // end of map()
                .orElseGet(() -> {
                    throw new UserNotFoundException(id);
                });
    }

    // delete user by id
    @DeleteMapping("/users/{id}")
    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    public void deleteAuthUser(@PathVariable Long id) {
        LOG.warn("Deleting auth user with id = {}.", id);

        // delete if exists - operation should be idempotent
        authUserRepository.findById(id).ifPresent(user -> authUserRepository.deleteById(id));
    }

    // Find all roles
    @GetMapping("/roles")
    public Iterable<AuthRole> listRoles() {
        LOG.debug("AuthRestController.listRoles() is working. ");
        return authRoleRepository.findAll();
    }

    // Save role; return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/roles")
    public AuthRole newAuthRole(@RequestBody AuthRole newAuthRole) {
        LOG.debug("AuthRestController.newAuthRole() is working.");
        return authRoleRepository.save(newAuthRole);
    }

    // Find role by id
    @GetMapping("/roles/{id}")
    public AuthRole findOneRole(@PathVariable Long id) {
        LOG.debug("AuthRestController.findOneRole() is working. Role id = {}.", id);
        return authRoleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
    }

    // Save or update role by id
    @PutMapping("/roles/{id}")
    public AuthRole saveOrUpdateAuthRole(@RequestBody AuthRole newAuthRole, @PathVariable Long id) {
        LOG.debug("AuthRestController.saveOrUpdateAuthRole() is working. Role id = {}.", id);

        return authRoleRepository.findById(id)
                .map(role -> { // role found, update - only description
                    LOG.debug("Role id = {} found. Updating with provided values.", id);
                    role.setDescription(newAuthRole.getDescription());
                    return authRoleRepository.save(role);
                })
                .orElseGet(() -> { // role not found - add new role
                    LOG.debug("User id = {} not found. Adding new role.", id);
                    newAuthRole.setId(id);
                    return authRoleRepository.save(newAuthRole);
                });
    }

    // Update role description only
    @PatchMapping("/roles/{id}")
    public AuthRole patchAuthRole(@RequestBody Map<String, String> update, @PathVariable Long id) {
        LOG.debug("AuthRestController.patchAuthRole() is working. Role id = {}.", id);

        return authRoleRepository.findById(id)
                .map(role -> {
                    String description = update.get("description");
                    if (!StringUtils.isEmpty(description)) {
                        role.setDescription(description);
                        // better create a custom method to update a value = :newValue where id = :id
                        return authRoleRepository.save(role);
                    } else {
                        throw new RoleUnSupportedFieldPatchException(update.keySet());
                    } // end of IF
                }) // end of map()
                .orElseGet(() -> {
                    throw new RoleNotFoundException(id);
                });
    }

    // delete role by id
    @DeleteMapping("/roles/{id}")
    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    public void deleteAuthRole(@PathVariable Long id) {
        LOG.warn("Deleting auth role with id = {}.", id);

        // delete if exists - operation should be idempotent
        authRoleRepository.findById(id).ifPresent(role -> authRoleRepository.deleteById(id));
    }

}
