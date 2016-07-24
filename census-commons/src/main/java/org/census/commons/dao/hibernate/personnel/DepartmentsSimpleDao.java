package org.census.commons.dao.hibernate.personnel;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dto.personnel.employees.EmployeeDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Dao component for working with Department domain object.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 30.07.2015)
 */

@Repository
@Transactional
public class DepartmentsSimpleDao extends AbstractHibernateDao<DepartmentDto> {

    public DepartmentsSimpleDao() {
        super(DepartmentDto.class);
    }

    /**
     * Method returns set of department's employees. If there is no such department, method returns empty set.
     * @param departmentId long department identity.
     */
    public Set<EmployeeDto> getEmployeesOfDept(long departmentId) {
        DepartmentDto department = this.findById(departmentId);
        return (department == null ? new HashSet<EmployeeDto>() : department.getEmployees());
    }

    /***/
    // todo: implement behavior: if no chief specified, return department's employee with the heaviest weighted position!
    public EmployeeDto getChiefOfDept(long departmentId) {
        DepartmentDto department = this.findById(departmentId);
        return (department == null ? null : department.getChief());
    }

}