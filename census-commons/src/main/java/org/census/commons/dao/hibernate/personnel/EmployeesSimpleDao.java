package org.census.commons.dao.hibernate.personnel;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.personnel.employees.EmployeeDto;
import org.hibernate.Query;
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

    /***/
    public EmployeeDto getEmployee(EmployeeDto employee) {

        if (employee == null) { //fail-fast
            throw new IllegalArgumentException("Can't search for null employee!");
        }

        Query query = this.getSessionFactory().getCurrentSession().createQuery(
                "select e from EmployeeDto as e where " +
                        "e.name = '" + employee.getName() + "' and e.family = '" + employee.getFamily() + "'" +
                        " and e.patronymic = '" + employee.getPatronymic() + "'"// + "' and " +
                        //"p.name in :plist"
        );
        //query.setParameterList("plist", new String[] {"Генеральный директор", "должность какаято"});

        //System.out.println("!!!");
        //Object obj = query.uniqueResult();
        //System.out.println(obj.getClass());
        //System.out.println("+++");

        //return null;
        return (EmployeeDto) query.uniqueResult();
    }

}