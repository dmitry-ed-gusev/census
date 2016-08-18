package org.census.commons.dao.hibernate.personnel;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.personnel.ContactTypeDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao component for working with Contect Type domain object.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 04.08.2015)
 */

@Repository
@Transactional
public class ContactsTypesSimpleDao extends AbstractHibernateDao<ContactTypeDto> {

    public ContactsTypesSimpleDao() {
        super(ContactTypeDto.class);
    }

    /**
     * Return ContactType object from DB by exact contact type name.
     * @param type String contact type name
     */
    public ContactTypeDto getContactTypeByType(String type) {
        return (ContactTypeDto) this.createQueryForCurrentSession(
                "select ct from ContactTypeDto as ct where ct.type = '" + type + "'")
                .uniqueResult();
    }

}