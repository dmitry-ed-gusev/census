package org.dgusev.census.auth.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dgusev.census.auth.domain.entity.AuthRole;
import org.dgusev.census.auth.exceptions.RoleUnSupportedFieldPatchException;
import org.dgusev.census.auth.repository.AuthRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static org.dgusev.census.auth.CensusAuthDefaults.*;

/** REST API controller for providing AuthRoles objects. */

@Slf4j
@RestController // <- allows write response directly to response body
@RequestMapping(path = URI_DEFAULT, produces = MediaType.APPLICATION_JSON_VALUE) // <- methods produce JSONs
@CrossOrigin(origins="*")
public class AuthRoleRestController {

    private AuthRoleRepository authRoleRepository;

    @Autowired
    public AuthRoleRestController(AuthRoleRepository authRoleRepository) {
        LOG.debug("AuthRoleRestController constructor() is working.");
        this.authRoleRepository = authRoleRepository;
    }

    // Find all roles
    @GetMapping(URI_ROLES)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<AuthRole> listRoles() {
        LOG.debug("AuthRoleRestController.listRoles() is working. ");
        return authRoleRepository.findAll();
    }

    // Save role; return 201 instead of 200
    @PostMapping(path = URI_ROLES, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AuthRole newAuthRole(@RequestBody AuthRole newAuthRole) {
        LOG.debug("AuthRoleRestController.newAuthRole() is working.");

        return authRoleRepository.save(newAuthRole);
    }

    // Find role by id (200 -> OK, 404 -> not found)
    @GetMapping(URI_ROLES_WITH_ID)
    public ResponseEntity<AuthRole> findOneRole(@PathVariable Long id) {
        LOG.debug("AuthRoleRestController.findOneRole() is working. Role id = {}.", id);

        // return authRoleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));

        Optional<AuthRole> authRole = authRoleRepository.findById(id);
        return authRole.map(role -> new ResponseEntity<>(role, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    // Save or update role by id
    @PutMapping(URI_ROLES_WITH_ID)
    public AuthRole saveOrUpdateAuthRole(@RequestBody AuthRole newAuthRole, @PathVariable Long id) {
        LOG.debug("AuthRoleRestController.saveOrUpdateAuthRole() is working. Role id = {}.", id);

        return authRoleRepository.findById(id)
                .map(role -> { // role found, update - only description
                    LOG.debug("Role id = {} found. Updating with provided values.", id);
                    if (!StringUtils.isBlank(newAuthRole.getDescription())) {
                        role.setDescription(newAuthRole.getDescription());
                    }

                    return authRoleRepository.save(role);
                })
                .orElseGet(() -> { // role not found - add new role
                    LOG.debug("User id = {} not found. Adding new role.", id);
                    newAuthRole.setId(id); // todo: id will be generated anyway, so provided ID won't be used
                    return authRoleRepository.save(newAuthRole);
                });
    }

    // Update role description only
    @PatchMapping(path = URI_ROLES_WITH_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthRole> patchAuthRole(@RequestBody Map<String, String> update, @PathVariable Long id) {
        LOG.debug("AuthRoleRestController.patchAuthRole() is working. Role id = {}.", id);

        return authRoleRepository.findById(id)
                .map(role -> {
                    String description = update.get("description");
                    if (!StringUtils.isEmpty(description)) {
                        role.setDescription(description);
                        // better create a custom method to update a value = :newValue where id = :id
                        return new ResponseEntity<>(authRoleRepository.save(role), HttpStatus.OK);
                    } else {
                        throw new RoleUnSupportedFieldPatchException(update.keySet());
                    } // end of IF
                }) // end of map()
                .orElseGet(() -> {
                    // throw new RoleNotFoundException(id);
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                });
    }

    // delete role by id
    @DeleteMapping(URI_ROLES_WITH_ID)
    @ResponseStatus(code=HttpStatus.NO_CONTENT)
    public void deleteAuthRole(@PathVariable Long id) {
        LOG.warn("Deleting auth role with id = {}.", id);

        // delete if exists - operation should be idempotent
        authRoleRepository.findById(id).ifPresent(role -> authRoleRepository.deleteById(id));
    }


}
