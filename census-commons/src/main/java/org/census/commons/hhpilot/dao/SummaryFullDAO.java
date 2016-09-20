package org.census.commons.hhpilot.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.hr.SummaryFullDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * hhpilot full summary DAO
 *
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 08.11.12)
 */
public class SummaryFullDAO {

    private Log log = LogFactory.getLog(SummaryFullDAO.class);

    SimpleApplicantDAO applicantDAO = new SimpleApplicantDAO();
    MainVacancyDAO mainVacancyDAO = new MainVacancyDAO();
    SubVacancyFirstDAO subVacancyFirstDAO = new SubVacancyFirstDAO();
    SubVacancySecondDAO subVacancySecondDAO = new SubVacancySecondDAO();

    /**
     * find all record
     *
     * @param connectionDB - connect to data
     * @return list full summary
     */
    public List<SummaryFullDTO> findAll(Connection connectionDB) {

        log.debug("findAll (Connection connectionDB)");

        String sql;
        List<SummaryFullDTO> summaryAll = new ArrayList<SummaryFullDTO>();
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select summary.id, summary.idapplicant, summary.phone, summary.email, summary.summarytextrus, summary.summarytexteng," +
                    " summary.idmainvacancy, summary.idsubmainfirstvacancy, summary.idsubmainsecondvacancy," +
                    " summary.updateuserid, mainvacancy.deleted, mainvacancy.timestamp " +
                    " from summary";

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    summaryAll.add(new SummaryFullDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("phone"), rs.getString("email"), rs.getString("summarytextrus"), rs.getString("summarytexteng"),
                            rs.getInt("idmainvacancy") > 0 ? mainVacancyDAO.findOneById(connectionDB, rs.getInt("idmainvacancy")) : null,
                            rs.getInt("idsubmainfirstvacancy") > 0 ? subVacancyFirstDAO.findOneById(connectionDB, rs.getInt("idsubmainfirstvacancy")) : null,
                            rs.getInt("idsubmainsecondvacancy") > 0 ? subVacancySecondDAO.findOneById(connectionDB, rs.getInt("idsubmainsecondvacancy")) : null,
                            applicantDAO.findOneById(connectionDB, rs.getInt("idapplicant"))));
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
     * @param idSummary    - id summary
     * @return one full summary
     */
    public SummaryFullDTO findOneById(Connection connectionDB, int idSummary) {

        log.debug("findById (Connection connectionDB, int idSummary)");

        String sql;
        SummaryFullDTO summary = null;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select summary.id, summary.idapplicant, summary.phone, summary.email, summary.summarytextrus, summary.summarytexteng," +
                    " summary.idmainvacancy, summary.idsubmainfirstvacancy, summary.idsubmainsecondvacancy," +
                    " summary.updateuserid, mainvacancy.deleted, mainvacancy.timestamp " +
                    " from summary " +
                    " where summary.id = " + idSummary;

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    summary = new SummaryFullDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("phone"), rs.getString("email"), rs.getString("summarytextrus"), rs.getString("summarytexteng"),
                            rs.getInt("idmainvacancy") > 0 ? mainVacancyDAO.findOneById(connectionDB, rs.getInt("idmainvacancy")) : null,
                            rs.getInt("idsubmainfirstvacancy") > 0 ? subVacancyFirstDAO.findOneById(connectionDB, rs.getInt("idsubmainfirstvacancy")) : null,
                            rs.getInt("idsubmainsecondvacancy") >0 ? subVacancySecondDAO.findOneById(connectionDB, rs.getInt("idsubmainsecondvacancy")) : null,
                            applicantDAO.findOneById(connectionDB, rs.getInt("idapplicant")));
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
     * update record
     *
     * @param connectionDB      - connect to data
     * @param summaryFullUpdate - full summary for update
     * @return 0 - update failed, else 1
     */
    public int updateSummary(Connection connectionDB, SummaryFullDTO summaryFullUpdate) {

        log.debug("updateSummary (Connection connectionDB, SummaryFullDTO summaryFullUpdate)");

        String sql;
        int resultUpdate = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "update summary set idapplicant = " + summaryFullUpdate.getApplicant().getId() +
                    ", phone = " + summaryFullUpdate.getPhone() +
                    ", email = " + summaryFullUpdate.getEmail() +
                    ", summarytextrus = " + summaryFullUpdate.getSummaryTextRus() +
                    ", summarytexteng = " + summaryFullUpdate.getSummaryTextEng() +
                    ", idmainvacancy = " + ((summaryFullUpdate.getMainVacancy() != null) ? summaryFullUpdate.getMainVacancy().getId() : 0) +
                    ", idsubmainfirstvacancy = " + ((summaryFullUpdate.getSubVacancyFirst() != null) ? summaryFullUpdate.getSubVacancyFirst().getId() : 0) +
                    ", idsubmainsecondvacancy = " + ((summaryFullUpdate.getSubVacancySecond() != null) ? summaryFullUpdate.getSubVacancySecond().getId() : 0) +
                    ", updateuserid = " + summaryFullUpdate.getUpdateUser() +
                    ", deleted = " + summaryFullUpdate.getDeleted() +
                    ", timestamp = " + summaryFullUpdate.getCreatedOn() +
                    " when summary.id = " + summaryFullUpdate.getId();

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                resultUpdate = 1;

            } catch (SQLException e) {
                log.error("error update data", e);
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
        return resultUpdate;
    }

    /**
     * insert record
     *
     * @param connectionDB      - connect to data
     * @param summaryFullInsert - full summary for insert
     * @return 0 - insert failed, else 1
     */
    public int insertSummary(Connection connectionDB, SummaryFullDTO summaryFullInsert) {

        log.debug("updateSummary (Connection connectionDB, SummaryFullDTO summaryFullUpdate)");

        String sql;
        int resultUpdate = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "insert summary (idapplicant, phone, email, summarytextrus, summarytexteng, idmainvacancy, idsubmainfirstvacancy, idsubmainsecondvacancy, updateuserid, deleted, timestamp) " +
                    " values (" + summaryFullInsert.getApplicant().getId() +
                    ", " + summaryFullInsert.getPhone() +
                    ", " + summaryFullInsert.getEmail() +
                    ", " + summaryFullInsert.getSummaryTextRus() +
                    ", " + summaryFullInsert.getSummaryTextEng() +
                    ", " + ((summaryFullInsert.getMainVacancy() != null) ? summaryFullInsert.getMainVacancy().getId() : 0) +
                    ", " + ((summaryFullInsert.getSubVacancyFirst() != null) ? summaryFullInsert.getSubVacancyFirst().getId() : 0) +
                    ", " + ((summaryFullInsert.getSubVacancySecond() != null) ? summaryFullInsert.getSubVacancySecond().getId() : 0) +
                    ", " + summaryFullInsert.getUpdateUser() +
                    ", " + summaryFullInsert.getDeleted() +
                    ", " + summaryFullInsert.getCreatedOn() + " )";

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                resultUpdate = 1;

            } catch (SQLException e) {
                log.error("error update data", e);
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
        return resultUpdate;
    }
}
