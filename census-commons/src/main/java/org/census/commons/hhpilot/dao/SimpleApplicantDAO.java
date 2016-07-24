package org.census.commons.hhpilot.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.hr.ApplicantDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * hhpilot applicant DAO
 *
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 08.11.12)
 */
public class SimpleApplicantDAO {

    private Log log = LogFactory.getLog(SimpleApplicantDAO.class);

    /**
     * find all record
     *
     * @param connectionDB - connect to data
     * @return list applicant
     */
    public List<ApplicantDTO> findAll (Connection connectionDB){

        log.debug("findAll (Connection connectionDB)");

        String sql;
        List<ApplicantDTO> applicantAll = new ArrayList<ApplicantDTO>();
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null){
            sql = "select applicant.id, applicant.fiorus, applicant.fioeng, applicant.sex, applicant.comments, " +
                    "applicant.dateinput, applicant.photo, applicant.updateuserid, applicant.deleted, applicant.timestamp " +
                    "from applicant";

            try{
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()){
                    // todo: !!!!
                    /*
                    applicantAll.add(new ApplicantDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("fiorus"), rs.getString("fioeng"), rs.getBoolean("sex"),
                            rs.getString("comment"), rs.getDate("dateinput"), rs.getString("photo")));
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
        return applicantAll;
    }

    /**
     * find one record
     *
     * @param connectionDB - connect to data
     * @param applicantId - applicant id
     * @return applicant
     */
    public ApplicantDTO findOneById (Connection connectionDB, int applicantId){

        log.debug("findAll (Connection connectionDB, int applicantId)");

        String sql;
        ApplicantDTO applicant = null;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null){
            sql = "select applicant.id, applicant.fiorus, applicant.fioeng, applicant.sex, applicant.comments, " +
                    "applicant.dateinput, applicant.photo, applicant.updateuserid, applicant.deleted, applicant.timestamp " +
                    "from applicant " +
                    "where applicant.id = " + applicantId;

            try{
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()){

                    // todo: !!!
                    /*
                    applicant = new ApplicantDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("fiorus"), rs.getString("fioeng"), rs.getBoolean("sex"),
                            rs.getString("comment"), rs.getDate("dateinput"), rs.getString("photo"));
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
        return applicant;
    }

    /**
     * update record
     *
     * @param connectionDB      - connect to data
     * @param applicantUpdate - applicant for update
     * @return 0 - update failed, else 1
     */
    public int updateApplicant(Connection connectionDB, ApplicantDTO applicantUpdate) {

        log.debug("updateApplicant(Connection connectionDB, ApplicantDTO applicantUpdate)");

        String sql;
        int resultUpdate = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "update applicant set fiorus = " + applicantUpdate.getFioRus() +
                    ", fioeng = " + applicantUpdate.getFioEng() +
                    ", sex = " + applicantUpdate.isSex() +
                    ", comment = " + applicantUpdate.getComments() +
                    ", dateinput = " + applicantUpdate.getDateInput() +
                    ", photo = " + applicantUpdate.getPhoto() +
                    ", updateuserid = " + applicantUpdate.getUpdateUser() +
                    ", deleted = " + applicantUpdate.getDeleted() +
                    ", timestamp = " + applicantUpdate.getTimestamp() +
                    " when applicant.id = " + applicantUpdate.getId();

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
     * @param applicantInsert - applicant for insert
     * @return 0 - insert failed, else 1
     */
    public int insertApplicant(Connection connectionDB, ApplicantDTO applicantInsert) {

        log.debug("insertApplicant(Connection connectionDB, ApplicantDTO applicantUpdate)");

        String sql;
        int resultUpdate = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "insert into applicant (fiorus, fioeng, sex, comment, dateinput, photo, updateuserid, deleted, timestamp) " +
                    " values (" + applicantInsert.getFioRus() +
                    ", " + applicantInsert.getFioEng() +
                    ", " + applicantInsert.isSex() +
                    ", " + applicantInsert.getComments() +
                    ", " + applicantInsert.getDateInput() +
                    ", " + applicantInsert.getPhoto() +
                    ", " + applicantInsert.getUpdateUser() +
                    ", " + applicantInsert.getDeleted() +
                    ", " + applicantInsert.getTimestamp() + ")";

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
