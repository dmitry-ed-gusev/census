package org.census.commons.dao.hibernate.personnel;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.personnel.ContactDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao component for working with Contact domain entity object.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 04.08.2015)
 */

@Repository
@Transactional
public class ContactsSimpleDao extends AbstractHibernateDao<ContactDto> {

    public ContactsSimpleDao() {
        super(ContactDto.class);
    }

}