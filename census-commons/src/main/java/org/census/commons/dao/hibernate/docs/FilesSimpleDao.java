package org.census.commons.dao.hibernate.docs;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.docs.FileDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao component for working with files domain objects.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 12.08.2015)
 */

@Repository
@Transactional
public class FilesSimpleDao extends AbstractHibernateDao<FileDto> {

    public FilesSimpleDao() {
        super(FileDto.class);
    }

}