package org.census.commons.dao.hibernate.admin;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.admin.LogicGroupDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao component/service for working with domain object Logic Group.
 * @author Gusev Dmitry
 * @version 1.0 (DATE: 08.04.2014)
 */

@Repository
// @Service("logicGroupsService") // <- we can use just one of annotations: @Service or @Repository
@Transactional
public class LogicGroupsSimpleDao extends AbstractHibernateDao<LogicGroupDto> {

    public LogicGroupsSimpleDao() {
        super(LogicGroupDto.class);
    }

}