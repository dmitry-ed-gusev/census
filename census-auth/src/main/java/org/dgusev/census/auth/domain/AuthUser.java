package org.dgusev.census.auth.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/***/

@Slf4j
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "auth_user")
public class AuthUser {

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String  name;

    private String  description;

    @NotNull
    @Size(min = 3, message = "Minimum length of user name should be 3 characters!")
    @Size(max = 30, message = "Maximum length of user name shouldn't be more 30 characters!")
    private String  username;

    @NotNull
    @Size(min = 3, message = "Minimum length of password should be 3 characters!")
    @Size(max = 30, message = "Maximum length of password shouldn't be more 30 characters!")
    private String password; // todo: possibly we don't need password here

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "createdat", updatable = false, insertable = false)
    private Date    createdAt;

    @Column(name = "modifiedat", updatable = false, insertable = false)
    private Date    modifiedAt;

    @ManyToMany(targetEntity=AuthRole.class)
    @JoinTable(name = "auth_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Size(min=1, message="You must choose at least 1 role!")
    private List<AuthRole> roles;

    @PrePersist
    public void prePersist() {
        LOG.debug("AuthUser.prePersist() is working. AuthUser: {}", this.toString());
    }

    @PreUpdate
    public void preUpdate() {
        LOG.debug("AuthUser.preUpdate() is working. AuthUser: {}", this.toString());
    }

}
