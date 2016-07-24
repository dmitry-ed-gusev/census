package org.census.commons.hhpilot.dao.interfaces;


import org.census.commons.dto.hr.VacancyDto;

import java.sql.SQLException;
import java.util.List;

/**
 * Dao interface for working with vacancy (domain class).
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 03.12.12)
 */
public interface VacancyDaoInterface {

    /**get all list vacancy*/
    public List<VacancyDto> getAllVacancy() throws SQLException;

    /**find vacancy by id*/
    public VacancyDto getVacancyById(int id) throws SQLException;

}
