package org.census.commons.dao.jpa;

import org.census.commons.dao.AbstractJpaDao;
import org.census.commons.dto.personnel.departments.DepartmentDto;

/**
 * @author Gusev Dmitry (dgusev)
 * @version 1.0 (DATE: 10.05.12)
 * @deprecated (for a time)
*/

//@Repository
//@Transactional
public class DepartmentJpaDao extends AbstractJpaDao<DepartmentDto> {

    public void setClazz() {
        super.setClazz(DepartmentDto.class);
    }

}