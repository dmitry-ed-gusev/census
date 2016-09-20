package org.census.commons.dao.hibernate.personnel;

import org.apache.commons.lang3.StringUtils;
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
        return (PositionDto) this.getSessionFactory().getCurrentSession().createQuery(
                "select p from PositionDto as p where p.name = '" + name + "'"
        ).uniqueResult();
    }

    /***/
    public PositionDto addPositionByName(String name) {

        if (StringUtils.isBlank(name)) { //fail-fast
            throw new IllegalArgumentException("Can't add position by null name!");
        }

        PositionDto position = this.getPositionByName(name);
        if (position == null) { // no such position
            position = new PositionDto(0, name);
            this.save(position);
        }

        return position;
    }

}