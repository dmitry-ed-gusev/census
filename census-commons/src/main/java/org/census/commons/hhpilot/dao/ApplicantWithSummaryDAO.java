package org.census.commons.hhpilot.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.hr.ApplicantWithSummaryDTO;

import java.sql.Connection;

/**
 * hhpilot applicant with summary DAO
 *
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 08.11.12)
 */
public class ApplicantWithSummaryDAO {

    private Log log = LogFactory.getLog(ApplicantWithSummaryDAO.class);

    /**
     * find applicant with all summary
     *
     * @param connectionDB - connect to data
     * @param idAppicant - id applicant
     * @return applicant with list summary
     */
    public ApplicantWithSummaryDTO findByIdWithFullSummary (Connection connectionDB, int idAppicant){

        log.debug("findByIdWithFullSummary (Connection connectionDB, int idAppicant)");

        ApplicantWithSummaryDTO applicantFull = null;

        if (connectionDB != null){

            applicantFull = new ApplicantWithSummaryDTO(new SimpleApplicantDAO().findOneById(connectionDB, idAppicant), new SummaryWithVacancyDAO().findAllSummaryForApplicant(connectionDB, idAppicant));

        } else {
            log.error("error connecting data");
        }
        return applicantFull;
    }
}
