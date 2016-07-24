package org.census.commons.dao.hibernate.personnel;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.personnel.employees.EmployeeWithDocsDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 13.08.2015)
 */

@Repository
@Transactional
public class EmployeesWithDocsDao extends AbstractHibernateDao<EmployeeWithDocsDto> {

    public EmployeesWithDocsDao() {
        super(EmployeeWithDocsDto.class);
    }

}