package org.dgusev.census.auth.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/***/

@Entity
@Table(name = "auth_role")
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class AuthRole {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private final Long id;

    @NotNull
    @Size(min = 3, message = "Minimum length of role name should be 3 characters!")
    @Size(max = 30, message = "Maximum length of role name shouldn't be more 30 characters!")
    private final String rolename;


    private final String description;

    @NotNull
    @Column(name = "createdat")
    private final Date   createdAt;

    @Column(name = "modifiedat")
    private final Date   modifiedAt;

    // todo: another way to set / update timastamp
    //@PrePersist
    //void createdAt() {
    //    this.createdAt = new Date();
    //}

}
