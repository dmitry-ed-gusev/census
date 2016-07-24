package org.census.commons.dao.hibernate.docs;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.docs.DocumentDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao component for working with Document domain object.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 11.08.2015)
 */

@Repository
@Transactional
public class DocumentsSimpleDao extends AbstractHibernateDao<DocumentDto> {

    protected DocumentsSimpleDao() {
        super(DocumentDto.class);
    }

}