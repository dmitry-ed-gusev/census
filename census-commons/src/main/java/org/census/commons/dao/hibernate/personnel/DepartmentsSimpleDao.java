package org.census.commons.dao.hibernate.personnel;

import org.apache.commons.lang3.StringUtils;
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

    /***/
    public DepartmentDto getDepartmentByName(String name) {
        return (DepartmentDto) this.getSessionFactory().getCurrentSession().createQuery(
                "select d from DepartmentDto as d where d.name = '" + name + "'"
        ).uniqueResult();
    }

    /***/
    public DepartmentDto addDepartmentByName(String name) {

        if (StringUtils.isBlank(name)) { // fail-fast
            throw new IllegalArgumentException("Can't add department by null name!");
        }

        DepartmentDto department = this.getDepartmentByName(name);
        if (department == null) {
            department = new DepartmentDto(0, name);
            this.save(department);
        }

        return department;
    }

}