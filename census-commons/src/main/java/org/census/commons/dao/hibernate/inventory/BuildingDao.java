package org.census.commons.dao.hibernate.inventory;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.inventory.BuildingDto;

/**
 * Dao component for entity BuildingDto.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 11.11.2014)
*/

//@Repository
//@Transactional
public class BuildingDao extends AbstractHibernateDao<BuildingDto> {

    public BuildingDao() {
        super(BuildingDto.class);
    }

}