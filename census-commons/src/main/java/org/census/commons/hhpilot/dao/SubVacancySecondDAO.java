package org.census.commons.hhpilot.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.hr.SubVacancySecondDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * hhpilot sub main second vacancy applicant DAO
 *
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 07.11.12)
 */
public class SubVacancySecondDAO {


    private Log log = LogFactory.getLog(SubVacancySecondDAO.class);

    /**
     * find all record
     *
     * @param connectionDB - connect to data
     * @return list sub main 2 vacancy
     */
    public List<SubVacancySecondDTO> findAll(Connection connectionDB) {

        log.debug("findAll (Connection connectionDB)");

        String sql;
        List<SubVacancySecondDTO> subMain2VacancyAll = new ArrayList<SubVacancySecondDTO>();
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select submainsecondvacancy.id, submainsecondvacancy.vacancynamerus, submainsecondvacancy.vacancynameeng, submainsecondvacancy.updateuserid, submainsecondvacancy.deleted, submainsecondvacancy.timestamp " +
                    "from submainsecondvacancy";

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    // todo: !!!
                    /*
                    subMain2VacancyAll.add(new SubVacancySecondDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
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
        return subMain2VacancyAll;
    }


    /**
     * find one record by id
     *
     * @param connectionDB  - connect to data
     * @param idSubMainSecondVacancy - id sub main 2 vacancy
     * @return sub main 2 vacancy by id
     */
    public SubVacancySecondDTO findOneById(Connection connectionDB, int idSubMainSecondVacancy) {

        log.debug("findOneById (Connection connectionDB, int idSubMainSecondVacancy)");

        String sql;
        SubVacancySecondDTO subMain2VacancyById = null;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select submainsecondvacancy.id, submainsecondvacancy.vacancynamerus, submainsecondvacancy.vacancynameeng, submainsecondvacancy.updateuserid, submainsecondvacancy.deleted, submainsecondvacancy.timestamp " +
                    " from submainsecondvacancy" +
                    " where submainsecondvacancy.id = " + idSubMainSecondVacancy;

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    // todo: !!!
                    /*
                    subMain2VacancyById = new SubVacancySecondDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
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
        return subMain2VacancyById;
    }


    /**
     * update record
     *
     * @param connectionDB      - connect to data
     * @param subMainSecondVacancyUpdate - sub main 2 vacancy for update
     * @return 0 - update failed, else 1
     */
    public int updateSubMainSecondVacancy(Connection connectionDB, SubVacancySecondDTO subMainSecondVacancyUpdate) {

        log.debug("updateSubMainSecondVacancy (Connection connectionDB, SubVacancySecondDTO subMainSecondVacancyUpdate)");

        String sql;
        int resultUpdate = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "update submainsecondvacancy set vacancynamerus = " + subMainSecondVacancyUpdate.getNameRus() +
                    ", vacancynameeng = " + subMainSecondVacancyUpdate.getNameEng() +
                    ", updateuserid = " + subMainSecondVacancyUpdate.getUpdateUser() +
                    ", deleted = " + subMainSecondVacancyUpdate.getDeleted() +
                    ", timestamp = " + subMainSecondVacancyUpdate.getCreatedOn() +
                    " when submainsecondvacancy.id = " + subMainSecondVacancyUpdate.getId();

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
     * @param subMainSecondVacancyInsert - sub main 2 vacancy for insert
     * @return 0 - insert failed, else 1
     */
    public int insertSubMainSecondVacancy(Connection connectionDB, SubVacancySecondDTO subMainSecondVacancyInsert) {

        log.debug("insertSubMainSecondVacancy (Connection connectionDB, SubVacancySecondDTO subMainSecondVacancyInsert)");

        String sql;
        int resultInsert = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "insert into submainsecondvacancy (vacancynamerus, vacancynameeng, updateuserid, deleted, timestamp)" +
                    " values (" + subMainSecondVacancyInsert.getNameRus() +
                    ", " + subMainSecondVacancyInsert.getNameEng() +
                    ", " + subMainSecondVacancyInsert.getUpdateUser() +
                    ", " + subMainSecondVacancyInsert.getDeleted() +
                    ", " + subMainSecondVacancyInsert.getCreatedOn() + " )";

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
