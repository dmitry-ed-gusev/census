package org.dgusev.census.auth.domain.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/***/

@Slf4j
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "auth_role")
public class AuthRole {

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY) // <- identity sequence per table
    // (strategy = GenerationType.AUTO) <- global sequence for all tables
    private Long id;

    @NotNull
    @Size(min = 3, message = "Minimum length of role name should be 3 characters!")
    @Size(max = 30, message = "Maximum length of role name shouldn't be more 30 characters!")
    private String rolename;


    private String description;

    @Column(name = "createdat", updatable = false, insertable = false)
    private Date   createdAt;

    @Column(name = "modifiedat", updatable = false, insertable = false)
    private Date   modifiedAt;

    @PrePersist
    public void prePersist() {
        LOG.debug("AuthRole.prePersist() is working. AuthRole: {}", this.toString());
    }

    @PreUpdate
    public void preUpdate() {
        LOG.debug("AuthRole.preUpdate() is working. AuthRole: {}", this.toString());
    }

}
