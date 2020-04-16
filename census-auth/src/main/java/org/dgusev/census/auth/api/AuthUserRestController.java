package org.dgusev.census.auth.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dgusev.census.auth.domain.entity.AuthUser;
import org.dgusev.census.auth.exceptions.UserUnSupportedFieldPatchException;
import org.dgusev.census.auth.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static org.dgusev.census.auth.CensusAuthDefaults.*;

/** REST API controller for providing AuthUsers objects. */

// todo: https://mkyong.com/spring-boot/spring-rest-validation-example/
// todo: https://www.baeldung.com/http-put-patch-difference-spring

// todo: implement add user to the role / remove user from the role

@Slf4j
@RestController // <- allows write response directly to response body
@RequestMapping(path = URI_DEFAULT, produces = MediaType.APPLICATION_JSON_VALUE) // <- methods produce JSONs
@CrossOrigin(origins="*")
public class AuthUserRestController {

    private AuthUserRepository authUserRepository;

    @Autowired
    public AuthUserRestController(AuthUserRepository authUserRepository) {
        LOG.debug("AuthUserRestController constructor() is working.");
        this.authUserRepository = authUserRepository;
    }

    // Find all users with assigned roles
    @GetMapping(URI_USERS)
    @ResponseStatus(HttpStatus.OK) // <- usually default status on success
    public Iterable<AuthUser> listUsers() {
        LOG.debug("AuthUserRestController.listUsers() is working.");
        return authUserRepository.findAll();
    }

    // Find user by id (200 -> OK, 404 -> not found)
    @GetMapping(URI_USERS_WITH_ID)
    public ResponseEntity<AuthUser> findOneUser(@PathVariable Long id) {
        LOG.debug("AuthUserRestController.findOneUser() is working. User id = {}.", id);

        // option 1: throw exception if user is not found
        // return authUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        // option 2: return NOT_FOUND (404) if user is not found, OK (200) otherwise
        Optional<AuthUser> authUser = authUserRepository.findById(id);
        return authUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    // Save user; return 201 instead of 200 (on success)
    @PostMapping(path = URI_USERS, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUser newAuthUser(@RequestBody AuthUser newAuthUser) {
        LOG.debug("AuthUserRestController.newAuthUser() is working.");

        return authUserRepository.save(newAuthUser);
    }

    // Save or update user by id (PUT - replace resource entirely). Won't set null / empty values.
    // We will update: name, description, username, password, activity status.
    // Operation is idempotent.
    @PutMapping(path = URI_USERS_WITH_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthUser saveOrUpdateAuthUser(@RequestBody AuthUser newAuthUser, @PathVariable Long id) {
        LOG.debug("AuthUserRestController.saveOrUpdateAuthUser() is working. User id = {}.", id);

        return authUserRepository.findById(id)
                .map(user -> { // updating found user
                    LOG.debug("User id = {} found. Updating with provided values.", id);
                    if (!StringUtils.isBlank(newAuthUser.getName())) {  // change user name
                        user.setName(newAuthUser.getName());
                    }

                    if (!StringUtils.isBlank(newAuthUser.getDescription())) { // change user description
                        user.setDescription(newAuthUser.getDescription());
                    }

                    if (!StringUtils.isBlank(newAuthUser.getUsername())) { // change username
                        user.setUsername(newAuthUser.getUsername());
                    }

                    if (!StringUtils.isBlank(newAuthUser.getPassword())) { // change user password
                        user.setPassword(newAuthUser.getPassword());
                    }

                    user.setActive(newAuthUser.isActive()); // change activity status

                    return authUserRepository.save(user); // update (save) user and return it
                })
                .orElseGet(() -> { // creating new user
                    // todo: disable adding of nw user - PUT is only for update for existing one
                    // todo: see PATCH method below
                    LOG.debug("User id = {} not found. Adding new user.", id);
                    newAuthUser.setId(id); // todo: id will be generated anyway, so provided ID won't be used
                    return authUserRepository.save(newAuthUser);
                });
    }

    // Update existing auth user (PATCH - partial update of the resource). Won't set null / empty values.
    // We will update: name, description, username, password, activity status.
    // Operation is idempotent.
    @PatchMapping(path = URI_USERS_WITH_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthUser> patchAuthUser(@RequestBody Map<String, String> update, @PathVariable Long id) {
        LOG.debug("AuthUserRestController.patchAuthUser() is working. User id = {}.", id);

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
                        return new ResponseEntity<>(authUserRepository.save(user), HttpStatus.OK);
                    } else {
                        throw new UserUnSupportedFieldPatchException(update.keySet());
                    } // end of IF
                }) // end of map()
                .orElseGet(() -> {
                    // throw new UserNotFoundException(id);
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                });
    }

    // delete user by id, always return 204 (no content)
    @DeleteMapping(URI_USERS_WITH_ID)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteAuthUser(@PathVariable Long id) {
        LOG.warn("Deleting auth user with id = {}.", id);

        // delete if exists - operation should be idempotent
        authUserRepository.findById(id).ifPresent(user -> authUserRepository.deleteById(id));
    }

}
