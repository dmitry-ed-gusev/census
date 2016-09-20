package org.census.commons.dao.hibernate.personnel;

import org.apache.commons.lang3.StringUtils;
import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.ContactType;
import org.census.commons.dto.personnel.ContactDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao component for working with Contact domain object.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 04.08.2015)
 */

@Repository
@Transactional
public class ContactsSimpleDao extends AbstractHibernateDao<ContactDto> {

    public ContactsSimpleDao() {
        super(ContactDto.class);
    }

    /***/
    // todo: add contact type to checking?
    public ContactDto getContactByContent(String content) {
        return (ContactDto) this.getSessionFactory().getCurrentSession().createQuery(
                "select c from ContactDto as c where c.contact = '" + content + "'"
        ).uniqueResult();
    }

    /***/
    public ContactDto addContactByContent(String content, ContactType type) {

        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Can't add contact by null content!");
        }

        ContactDto contact = this.getContactByContent(content);
        if (contact == null) {
            contact = new ContactDto(0, content, null, type);
            this.save(contact);
        }

        return contact;
    }

}