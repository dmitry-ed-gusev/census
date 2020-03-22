package org.dgusev.census.auth.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/***/

@Entity
@Table(name = "auth_user")
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class AuthUser {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private final Long id;

    @NotNull
    private final String  name;
    private final String  description;
    @NotNull
    @Size(min = 3, message = "Minimum length of user name should be 3 characters!")
    @Size(max = 30, message = "Maximum length of user name shouldn't be more 30 characters!")
    private final String  username;
    private final boolean isActive;
    @NotNull
    private final Date    createdAt;
    private final Date    modifiedAt;

    @ManyToMany(targetEntity=AuthRole.class)
    @JoinTable(name = "auth_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Size(min=1, message="You must choose at least 1 role!")
    private List<AuthRole> roles;

}
