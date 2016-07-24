package org.census.commons.dao.jpa;

import org.census.commons.dao.AbstractJpaDao;
import org.census.commons.dto.personnel.employees.EmployeeDto;

/**
 * @author Gusev Dmitry (dgusev)
 * @version 1.0 (DATE: 10.05.12)
 * @deprecated (for a time)
*/

//@Repository
//@Transactional
public class EmployeeJpaDao extends AbstractJpaDao<EmployeeDto> {

    public EmployeeJpaDao() {
        this.setClazz(EmployeeDto.class);
    }

    //public void setClazz() {
    // super.setClazz(EmployeeDto.class);
    //}

}