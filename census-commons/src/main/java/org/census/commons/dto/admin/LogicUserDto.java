package org.census.commons.dto.admin;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.CensusDefaults;
import org.census.commons.dto.AbstractEntityDto;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain object - system logic user (login/password pair). Related to logic group domain object, relation
 * is many-to-many (one user can belong to many groups, one group contains many users).
 * @author Gusev Dmitry (dgusev)
 * @version 1.0 (DATE: 05.08.2015)
*/

@Entity
@Table(name = "LOGICUSERS")
@Indexed
public class LogicUserDto extends AbstractEntityDto {

    @Column(name = "fullName")
    private String      fullName;
    @Column(name = "login")
    private String      login;
    @Column(name = "password")
    private String      password;
    @Column(name = "email")
    private String      email;
    @Column(name = "locked")
    private int         locked;

    // Mapping many-to-many to Logic Groups (both entity and db table). This side (LogicUserDto) is
    // relation owner. Relation is bi-directional.
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "link_logicusers_2_logicgroups",
            joinColumns        = {@JoinColumn(name = "logicUserId",  nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "logicGroupId", nullable = false, updatable = false)})
    private Set<LogicGroupDto> logicGroups = new HashSet<>(0);

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return this.locked != 0;
    }

    public Set<LogicGroupDto> getLogicGroups() {
        return logicGroups;
    }

    public void setLogicGroups(Set<LogicGroupDto> logicGroups) {
        this.logicGroups = logicGroups;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, CensusDefaults.CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("fullName", fullName)
                .append("login", login)
                .append("password", password)
                .append("email", email)
                .append("locked", locked)
                .append("logicGroups", logicGroups)
                .toString();
    }

}