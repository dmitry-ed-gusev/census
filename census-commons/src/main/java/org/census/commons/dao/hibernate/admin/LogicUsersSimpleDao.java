package org.census.commons.dao.hibernate.admin;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.admin.LogicUserDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao component for working with domain object Logic User (login/password pair).
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 31.07.2015)
*/

@Repository
@Transactional
public class LogicUsersSimpleDao extends AbstractHibernateDao<LogicUserDto> {

    public LogicUsersSimpleDao() {
        super(LogicUserDto.class);
    }

}