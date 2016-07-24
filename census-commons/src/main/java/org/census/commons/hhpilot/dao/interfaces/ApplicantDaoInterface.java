package org.census.commons.hhpilot.dao.interfaces;


import org.census.commons.hhpilot.domain.ApplicantDto;

import java.sql.SQLException;
import java.util.List;

/**
 * Dao interface for working with applicants (domain class).
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 14.11.12)
 */

public interface ApplicantDaoInterface {

    /**get all list applicant*/
    public List<ApplicantDto> getAllApplicants() throws SQLException;

    /**find applicant by id*/
    public ApplicantDto getApplicantById(int id) throws SQLException;

}