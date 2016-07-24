package org.census.commons.dao.jpa;

import org.census.commons.dao.AbstractJpaDao;
import org.census.commons.dto.personnel.PositionDto;

/**
 * @author Gusev Dmitry (dgusev)
 * @version 1.0 (DATE: 07.05.12)
 * @deprecated (for a time)
*/

//@Repository
//@Transactional
public class PositionJpaDao extends AbstractJpaDao<PositionDto> {

    public PositionJpaDao() {
        this.setClazz(PositionDto.class);
    }

}