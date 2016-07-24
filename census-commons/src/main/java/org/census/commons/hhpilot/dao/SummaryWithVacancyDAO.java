package org.census.commons.hhpilot.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.hr.SummaryWithVacancyDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * hhpilot summary with vacancy DAO
 *
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 08.11.12)
 */
public class SummaryWithVacancyDAO {


    private Log log = LogFactory.getLog(SummaryWithVacancyDAO.class);

    MainVacancyDAO mainVacancyDAO = new MainVacancyDAO();
    SubVacancyFirstDAO subVacancyFirstDAO = new SubVacancyFirstDAO();
    SubVacancySecondDAO subVacancySecondDAO = new SubVacancySecondDAO();

    /**
     * find all record
     *
     * @param connectionDB - connect to data
     * @return list summary with vacancy
     */
    public List<SummaryWithVacancyDTO> findAll(Connection connectionDB) {

        log.debug("findAll (Connection connectionDB)");

        String sql;
        List<SummaryWithVacancyDTO> summaryAll = new ArrayList<SummaryWithVacancyDTO>();
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select summary.id, summary.phone, summary.email, summary.summarytextrus, summary.summarytexteng," +
                    " summary.idmainvacancy, summary.idsubmainfirstvacancy, summary.idsubmainsecondvacancy," +
                    " summary.updateuserid, mainvacancy.deleted, mainvacancy.timestamp " +
                    " from summary";

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    // todo: !!!
                    /*
                    summaryAll.add(new SummaryWithVacancyDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("phone"), rs.getString("email"), rs.getString("summarytextrus"), rs.getString("summarytexteng"),
                            rs.getInt("idmainvacancy") > 0 ? mainVacancyDAO.findOneById(connectionDB, rs.getInt("idmainvacancy")) : null,
                            rs.getInt("idsubmainfirstvacancy") > 0 ? subVacancyFirstDAO.findOneById(connectionDB, rs.getInt("idsubmainfirstvacancy")) : null,
                            rs.getInt("idsubmainsecondvacancy") > 0 ? subVacancySecondDAO.findOneById(connectionDB, rs.getInt("idsubmainsecondvacancy")) : null));
                            */
                }

            } catch (SQLException e) {
                log.error("error of data", e);
            } finally {
                if (rs != null)
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        log.error("can't close result set ", e);
                    }
                if (stmt != null)
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        log.error("can't close statement ", e);
                    }
            }
        } else {
            log.error("error connecting data");
        }
        return summaryAll;
    }

    /**
     * find one record
     *
     * @param connectionDB - connect to data
     * @param idSummary - id summary
     * @return one summary with vacancy
     */
    public SummaryWithVacancyDTO findOneById(Connection connectionDB, int idSummary) {

        log.debug("findById(Connection connectionDB, int idSummary");

        String sql;
        SummaryWithVacancyDTO summary = null;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select summary.id, summary.phone, summary.email, summary.summarytextrus, summary.summarytexteng," +
                    " summary.idmainvacancy, summary.idsubmainfirstvacancy, summary.idsubmainsecondvacancy," +
                    " summary.updateuserid, mainvacancy.deleted, mainvacancy.timestamp " +
                    " from summary " +
                    " where summary.id = " + idSummary;

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    // todo: !!!
                                        /*
                    summary = new SummaryWithVacancyDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("phone"), rs.getString("email"), rs.getString("summarytextrus"), rs.getString("summarytexteng"),
                            rs.getInt("idmainvacancy") > 0 ? mainVacancyDAO.findOneById(connectionDB, rs.getInt("idmainvacancy")) : null,
                            rs.getInt("idsubmainfirstvacancy") > 0 ? subVacancyFirstDAO.findOneById(connectionDB, rs.getInt("idsubmainfirstvacancy")) : null,
                            rs.getInt("idsubmainsecondvacancy") > 0 ? subVacancySecondDAO.findOneById(connectionDB, rs.getInt("idsubmainsecondvacancy")) : null);
                            */
                }

            } catch (SQLException e) {
                log.error("error of data", e);
            } finally {
                if (rs != null)
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        log.error("can't close result set ", e);
                    }
                if (stmt != null)
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        log.error("can't close statement ", e);
                    }
            }
        } else {
            log.error("error connecting data");
        }
        return summary;
    }

    /**
     * find all record for one applicant
     *
     * @param connectionDB - connect to data
     * @param idAppicant - id applicant
     * @return list summary with vacancy for one applicant
     */
    public List<SummaryWithVacancyDTO> findAllSummaryForApplicant(Connection connectionDB, int idAppicant) {

        log.debug("findAllSummaryForAppicant(Connection connectionDB, int idAppicant)");

        String sql;
        List<SummaryWithVacancyDTO> summaryAll = new ArrayList<SummaryWithVacancyDTO>();
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select summary.id, summary.phone, summary.email, summary.summarytextrus, summary.summarytexteng," +
                    " summary.idmainvacancy, summary.idsubmainfirstvacancy, summary.idsubmainsecondvacancy," +
                    " summary.updateuserid, mainvacancy.deleted, mainvacancy.timestamp " +
                    " from summary " +
                    " where summary.idapplicant = " + idAppicant;

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    // todo: !!!
                                        /*
                    summaryAll.add(new SummaryWithVacancyDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("phone"), rs.getString("email"), rs.getString("summarytextrus"), rs.getString("summarytexteng"),
                            rs.getInt("idmainvacancy") > 0 ? mainVacancyDAO.findOneById(connectionDB, rs.getInt("idmainvacancy")) : null,
                            rs.getInt("idsubmainfirstvacancy") > 0 ? subVacancyFirstDAO.findOneById(connectionDB, rs.getInt("idsubmainfirstvacancy")) : null,
                            rs.getInt("idsubmainsecondvacancy") > 0 ? subVacancySecondDAO.findOneById(connectionDB, rs.getInt("idsubmainsecondvacancy")) : null));
                            */
                }

            } catch (SQLException e) {
                log.error("error of data", e);
            } finally {
                if (rs != null)
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        log.error("can't close result set ", e);
                    }
                if (stmt != null)
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        log.error("can't close statement ", e);
                    }
            }
        } else {
            log.error("error connecting data");
        }
        return summaryAll;
    }
}
