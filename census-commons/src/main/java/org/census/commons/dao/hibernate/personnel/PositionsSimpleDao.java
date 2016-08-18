package org.census.commons.dao.hibernate.personnel;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.personnel.PositionDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao component for working with Position domain object.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 29.07.2015)
 */

@Repository
@Transactional
public class PositionsSimpleDao extends AbstractHibernateDao<PositionDto> {

    public PositionsSimpleDao() {
        super(PositionDto.class);
    }

    /**
     * Return Position object from DB by exact position name.
     * @param name String position name
     */
    public PositionDto getPositionByName(String name) {
        String query = String.format("select p from PositionDto as p where p.name = '%s'", name);
        return (PositionDto) this.createQueryForCurrentSession(query).uniqueResult();
    }

}