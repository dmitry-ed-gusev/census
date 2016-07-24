package org.census.commons.hhpilot.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.hr.MainVacancyDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * hhpilot main vacancy applicant DAO
 *
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 07.11.12)
 */
public class MainVacancyDAO {

    private Log log = LogFactory.getLog(MainVacancyDAO.class);

    /**
     * find all record
     *
     * @param connectionDB - connect to data
     * @return list main vacancy
     */
    public List<MainVacancyDTO> findAll(Connection connectionDB) {

        log.debug("findAll (Connection connectionDB)");

        String sql;
        List<MainVacancyDTO> mainVacancyAll = new ArrayList<MainVacancyDTO>();
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select mainvacancy.id, mainvacancy.vacancynamerus, mainvacancy.vacancynameeng, mainvacancy.updateuserid, mainvacancy.deleted, mainvacancy.timestamp " +
                    "from mainvacancy";

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    // todo: !!!
                                        /*
                    mainVacancyAll.add(new MainVacancyDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
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
        return mainVacancyAll;
    }


    /**
     * find one record by id
     *
     * @param connectionDB  - connect to data
     * @param idMainVacancy - id main vacancy
     * @return main vacancy by id
     */
    public MainVacancyDTO findOneById(Connection connectionDB, int idMainVacancy) {

        log.debug("findOneById (Connection connectionDB, int idMainVacancy)");

        String sql;
        MainVacancyDTO mainVacancyById = null;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "select mainvacancy.id, mainvacancy.vacancynamerus, mainvacancy.vacancynameeng, mainvacancy.updateuserid, mainvacancy.deleted, mainvacancy.timestamp " +
                    " from mainvacancy" +
                    " where mainvacancy.id = " + idMainVacancy;

            try {
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    // todo: !!!
                                        /*
                    mainVacancyById = new MainVacancyDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
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
        return mainVacancyById;
    }


    /**
     * update record
     *
     * @param connectionDB      - connect to data
     * @param mainVacancyUpdate - main vacancy for update
     * @return 0 - update failed, else 1
     */
    public int updateMainVacancy(Connection connectionDB, MainVacancyDTO mainVacancyUpdate) {

        log.debug("updateMainVacancy (Connection connectionDB, MainVacancyDTO mainVacancyUpdate)");

        String sql;
        int resultUpdate = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "update mainvacancy set vacancynamerus = " + mainVacancyUpdate.getNameRus() +
                    ", vacancynameeng = " + mainVacancyUpdate.getNameEng() +
                    ", updateuserid = " + mainVacancyUpdate.getUpdateUser() +
                    ", deleted = " + mainVacancyUpdate.getDeleted() +
                    ", timestamp = " + mainVacancyUpdate.getTimestamp() +
                    " when mainvacancy.id = " + mainVacancyUpdate.getId();

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
     * @param mainVacancyInsert - main vacancy for insert
     * @return 0 - insert failed, else 1
     */
    public int insertMainVacancy(Connection connectionDB, MainVacancyDTO mainVacancyInsert) {

        log.debug("insertMainVacancy (Connection connectionDB, MainVacancyDTO mainVacancyInsert)");

        String sql;
        int resultInsert = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "insert into mainvacancy (vacancynamerus, vacancynameeng, updateuserid, deleted, timestamp)" +
                    " values (" + mainVacancyInsert.getNameRus() +
                    ", " + mainVacancyInsert.getNameEng() +
                    ", " + mainVacancyInsert.getUpdateUser() +
                    ", " + mainVacancyInsert.getDeleted() +
                    ", " + mainVacancyInsert.getTimestamp() + " )";

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
