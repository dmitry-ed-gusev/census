package org.census.commons.dto;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static org.census.commons.CensusDefaults.CS_ID_GENERATOR_NAME;

/**
 * Base abstract domain object with database identity. Incapsulates some common properties.
 * Domain entity objects (not value objects!) should extend this object.
 * Used Hibernate mapping strategy "Table per concrete class with polymorphism".
 * @author Gusev Dmitry
 * @version 1.0 (DATE: 10.09.16)
*/

@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // Entity unique persistent (DB) identifier. Used named generator (see package-info.java in
    // current package). Identifier will be generated BEFORE any insert operation to DB (that
    // operation may be deferred).
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = CS_ID_GENERATOR_NAME)
    private long   id;

    @Column(name = "COMMENT")
    private String comment;     // comment for current record/entity

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdOn", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date   createdOn;   // creation date/time, assigned by Hibernate

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedOn")
    @org.hibernate.annotations.UpdateTimestamp
    // todo: check timestamp updating during SQL UPDATE
    private Date   modifiedOn;   // last modification date/time, assigned by Hibernate

    @Column(name = "UPDATE_USER")
    private long   updateUser;  // entity modifier/creator (logical user ID)

    @Column(name = "DELETED")
    private int    deleted = 0; // record/entity status -> 0 = active, other = deleted

    protected AbstractEntity() {}

    protected AbstractEntity(long id) {
        this.id = id;
    }

    public long getId() {
       return id;
    }

    public String getComment() {
        return comment;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public long getUpdateUser() {
        return updateUser;
    }

    public int getDeleted() {
        return deleted;
    }

    @Override
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        // comparing
        AbstractEntity other = (AbstractEntity) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("comment", comment)
                .append("createdOn", createdOn)
                .append("modifiedOn", modifiedOn)
                .append("updateUser", updateUser)
                .append("deleted", deleted)
                .toString();
    }

}