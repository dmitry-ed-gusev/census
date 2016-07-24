package org.census.commons.hhpilot.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.hr.SubVacancyFirstDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * hhpilot sub main first vacancy applicant DAO
 *
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 07.11.12)
 */
public class SubVacancyFirstDAO {

    private Log log = LogFactory.getLog(SubVacancyFirstDAO.class);

    /**
     * find all record
     *
     * @param connectionDB - connect to data
     * @return list sub main 1 vacancy
     */
    public List<SubVacancyFirstDTO> findAll(Connection connectionDB) {

        log.debug("findAll (Connection connectionDB)");

        String sql;
        List<SubVacancyFirstDTO> subMain1VacancyAll = new ArrayList<SubVacancyFirstDTO>();
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select submainfirstvacancy.id, submainfirstvacancy.vacancynamerus, submainfirstvacancy.vacancynameeng, submainfirstvacancy.updateuserid, submainfirstvacancy.deleted, submainfirstvacancy.timestamp " +
                    "from submainfirstvacancy";

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    // todo: !!!
                                        /*
                    subMain1VacancyAll.add(new SubVacancyFirstDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("statusnamerus"), rs.getString("statusnameeng")));
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
        return subMain1VacancyAll;
    }


    /**
     * find one record by id
     *
     * @param connectionDB  - connect to data
     * @param idSubMainFirstVacancy - id sub main 1 vacancy
     * @return sub main 1 vacancy by id
     */
    public SubVacancyFirstDTO findOneById(Connection connectionDB, int idSubMainFirstVacancy) {

        log.debug("findOneById (Connection connectionDB, int idSubMainFirstVacancy)");

        String sql;
        SubVacancyFirstDTO subMain1VacancyById = null;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select submainfirstvacancy.id, submainfirstvacancy.vacancynamerus, submainfirstvacancy.vacancynameeng, submainfirstvacancy.updateuserid, submainfirstvacancy.deleted, submainfirstvacancy.timestamp " +
                    " from submainfirstvacancy" +
                    " where submainfirstvacancy.id = " + idSubMainFirstVacancy;

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    // todo: !!!
                                        /*
                    subMain1VacancyById = new SubVacancyFirstDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("vacancynamerus"), rs.getString("vacancynameeng"));
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
        return subMain1VacancyById;
    }


    /**
     * update record
     *
     * @param connectionDB      - connect to data
     * @param subMainFirstVacancyUpdate - sub main 1 vacancy for update
     * @return 0 - update failed, else 1
     */
    public int updateSubMainFirstVacancy(Connection connectionDB, SubVacancyFirstDTO subMainFirstVacancyUpdate) {

        log.debug("updateSubMainFirstVacancy (Connection connectionDB, SubVacancyFirstDTO subMainFirstVacancyUpdate)");

        String sql;
        int resultUpdate = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "update submainfirstvacancy set vacancynamerus = " + subMainFirstVacancyUpdate.getNameRus() +
                    ", vacancynameeng = " + subMainFirstVacancyUpdate.getNameEng() +
                    ", updateuserid = " + subMainFirstVacancyUpdate.getUpdateUser() +
                    ", deleted = " + subMainFirstVacancyUpdate.getDeleted() +
                    ", timestamp = " + subMainFirstVacancyUpdate.getTimestamp() +
                    " when submainfirstvacancy.id = " + subMainFirstVacancyUpdate.getId();

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
     * @param subMainFirstVacancyInsert - sub main 1 vacancy for insert
     * @return 0 - insert failed, else 1
     */
    public int insertSubMainFirstVacancy(Connection connectionDB, SubVacancyFirstDTO subMainFirstVacancyInsert) {

        log.debug("insertSubMainFirstVacancy (Connection connectionDB, SubVacancyFirstDTO subMainFirstVacancyInsert)");

        String sql;
        int resultInsert = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "insert into submainfirstvacancy (vacancynamerus, vacancynameeng, updateuserid, deleted, timestamp)" +
                    " values (" + subMainFirstVacancyInsert.getNameRus() +
                    ", " + subMainFirstVacancyInsert.getNameEng() +
                    ", " + subMainFirstVacancyInsert.getUpdateUser() +
                    ", " + subMainFirstVacancyInsert.getDeleted() +
                    ", " + subMainFirstVacancyInsert.getTimestamp() + " )";

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                resultInsert = 1;

            } catch (SQLException e) {
                log.error("error insert data", e);
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
        return resultInsert;
    }
}
