package org.census.commons.hhpilot.dao.interfaces;


import org.census.commons.hhpilot.domain.SummaryDto;

import java.sql.SQLException;
import java.util.List;

/**
 * Dao interface for working with summary (domain class).
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 03.12.12)
 */
public interface SummaryDaoInterface {

    /**get all list summary*/
    public List<SummaryDto> getAllSummary() throws SQLException;

    /**find summary by id*/
    public SummaryDto getSummaryById(int id) throws SQLException;

}
