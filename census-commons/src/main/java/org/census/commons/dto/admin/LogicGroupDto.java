package org.census.commons.dto.admin;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.CensusDefaults;
import org.census.commons.dto.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain object - one logic group. Related to logic user domain object, relation is many-to-many
 * (one user can belong to many groups, one group contains many users).
 * @author Gusev Dmitry (dgusev)
 * @version 1.0 (DATE: 05.08.2015)
*/

//@Entity
//@Table(name = "LOGICGROUPS")
//@Indexed
public class LogicGroupDto extends AbstractEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    // Mapping many-to-many to Logic Users (both entity and db table). This side (LogicGroupDto) is
    // relation slave. Relation is bi-directional.
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "logicGroups", cascade = CascadeType.ALL)
    private Set<LogicUserDto> logicUsers = new HashSet<>(0);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<LogicUserDto> getLogicUsers() {
        return logicUsers;
    }

    public void setLogicUsers(Set<LogicUserDto> logicUsers) {
        this.logicUsers = logicUsers;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, CensusDefaults.CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("name", name)
                .append("description", description)
                .append("logicUsers", logicUsers)
                .toString();
    }

}