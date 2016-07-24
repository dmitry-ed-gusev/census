package org.census.commons.dao.hibernate.personnel;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.personnel.employees.EmployeeDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Dao component for working with Employee domain object.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 07.07.2015)
 */

@Repository
@Transactional
public class EmployeesSimpleDao extends AbstractHibernateDao<EmployeeDto> {

    public EmployeesSimpleDao() {
        super(EmployeeDto.class);
    }

    /***/
    public List<EmployeeDto> getEmployeesByGroupName(String groupName) {
        // generate HQL query and get resulting list
        return this.getSessionFactory().getCurrentSession().createQuery(
                "select e from EmployeeDto as e inner join e.logicUsers as user " +
                "inner join user.logicGroups as group where group.name like '%" + groupName + "%'"
        ).list();
    }

    /***/
    public List<EmployeeDto> getEmployeesByPositionName(String positionName) {
        return this.getSessionFactory().getCurrentSession().createQuery(
                "select e from EmployeeDto as e inner join e.positions as position where position.name like '%" + positionName + "%'"
        ).list();
    }

    /**
     * Method returns Employee by login name. If nothing found, method returns null.
     * @param loginName String login value for employee search.
    */
    public EmployeeDto getEmployeeByLogin(String loginName) {
        return (EmployeeDto) this.getSessionFactory().getCurrentSession().createQuery(
                "select e from EmployeeDto as e inner join e.logicUsers as logicUser where logicUser.login like '%" + loginName + "%'"
        ).uniqueResult();
    }

}