package org.census.commons.dto.personnel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.CensusDefaults;
import org.census.commons.dto.AbstractEntityDto;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain object - type of contact (ContactDto object) - skype/email/... etc.
 * @author Gusev Dmitry (Dmitry)
 * @version 1.0 (DATE: 12.07.2015)
 */

@Entity
@Indexed
@Table(name = "CONTACTSTYPES")
public class ContactTypeDto extends AbstractEntityDto {

    @Column(name = "contactType")
    private String type;         // contact type value
    @Column(name = "description")
    private String description;  // contact type description

    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "contactType")
    private Set<ContactDto> contacts = new HashSet<>(0); // contacts with concrete type (this type)

    /** Default constructor. Usually used by frameworks Spring/Hibernate. */
    public ContactTypeDto() {}

    /***/
    public ContactTypeDto(long id, String type) {
        this.setId(id);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ContactDto> getContacts() {
        return contacts;
    }

    public void setContacts(Set<ContactDto> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, CensusDefaults.CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("type", type)
                .append("description", description)
                .append("contacts", contacts)
                .toString();
    }

}