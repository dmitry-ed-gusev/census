package org.census.commons.dao.hibernate.personnel;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.personnel.departments.DepartmentWithDocsDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 13.08.2015)
 */

@Repository
@Transactional
public class DepartmentsWithDocsDao extends AbstractHibernateDao<DepartmentWithDocsDto> {

    public DepartmentsWithDocsDao() {
        super(DepartmentWithDocsDto.class);
    }

}