package org.census.commons.dto.personnel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.census.commons.CensusDefaults;
import org.census.commons.dto.AbstractEntity;
import org.census.commons.dto.ContactType;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Domain entity object - contact info (email/real address/skype/... etc.).
 * Related to employees and departments.Can hold duplicates (by value - contact info).
 * @author Gusev Dmitry (Dmitry)
 * @version 1.0 (DATE: 12.07.2015)
 */

@Entity
@Indexed
@Table(name = "CONTACTS")
public class ContactDto extends AbstractEntity {

    @NotNull
    @Column(name = "CONTACT", nullable = false)
    private String         contact;     // contact value, mandatory property, not unique
    @Column(name = "DESCRIPTION")
    private String         description; // description of rhis contact
    @NotNull
    @Enumerated(EnumType.STRING)        // -> store string instead of default position number (in enum)
    @Column(name = "CONTACT_TYPE", nullable = false)
    private ContactType    contactType; // type of this contact (enum), mndatory, not nullable, not unique

    /** Default constructor. Usually used by frameworks Spring/Hibernate. */
    public ContactDto() {}

    /***/
    public ContactDto(long id, String contact, String description, ContactType type) {
        super(id);
        this.contact = contact;
        this.description = description;
        this.contactType = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    @Override
    @SuppressWarnings({"SimplifiableIfStatement", "MethodWithMultipleReturnPoints"})
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ContactDto)) return false;
        if (!super.equals(obj)) return false;

        ContactDto other = (ContactDto) obj;

        if (!contact.equals(other.contact)) return false;
        return contactType == other.contactType;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + contact.hashCode();
        result = 31 * result + contactType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, CensusDefaults.CS_DOMAIN_OBJECTS_STYLE)
                .appendSuper(super.toString())
                .append("contact", contact)
                .append("description", description)
                .append("contactType", contactType)
                .toString();
    }

}