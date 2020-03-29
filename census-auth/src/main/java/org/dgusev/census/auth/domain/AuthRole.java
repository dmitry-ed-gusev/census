package org.dgusev.census.auth.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/***/

@Entity
@Table(name = "auth_role")
@RequiredArgsConstructor
@Data
public class AuthRole {

    @Id @GeneratedValue (strategy= GenerationType.AUTO, generator = "auth_sequence")
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

}
