package org.census.commons.dto;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Base domain object (abstract). All other domain objects (concrete) will be inherited from this object.
 * @author Gusev Dmitry (Дмитрий)
 * @version 1.0 (DATE: 02.05.12)
*/

@MappedSuperclass // <- annotation for abstract DTO superclass
public abstract class AbstractEntityDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // Entity/DB record unique identifier.
    @Id @Column(name = "ID") @GeneratedValue (strategy = GenerationType.AUTO)
    // We're using auto-generated id's values (annotation @GeneratedValue) and Hiber returns last insert
    // id value (when perform INSERT query). Sequence id generator used for ORACLE DBMS (generator = '<name>').
    //@GeneratedValue (strategy = GenerationType.AUTO, generator = "id_generator")
    private long   id;

    @Column(name = "comment")
    private String comment;     // comment for current record/entity
    @Column(name = "timestamp")
    private Date   timestamp;   // register/last modify date/time
    @Column(name = "updateUser")
    private long   updateUser;  // entity modifier (logical user ID)
    @Column(name = "deleted")
    private int    deleted = 0; // record/entity status -> 0 = active, other = deleted

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(long updateUser) {
        this.updateUser = updateUser;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        // comparing
        AbstractEntityDto other = (AbstractEntityDto) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        //LogFactory.getLog(AbstractEntityDto.class).debug("AbstractEntityDto.toString()."); // <- too much output
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("comment", comment)
                .append("timestamp", timestamp)
                .append("updateUser", updateUser)
                .append("deleted", deleted)
                .toString();
    }

}