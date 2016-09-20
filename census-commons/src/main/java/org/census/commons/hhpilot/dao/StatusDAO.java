package org.census.commons.hhpilot.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.hr.StatusDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * hhpilot status applicant DAO
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 07.11.12)
 */
public class StatusDAO {

    private Log log = LogFactory.getLog(StatusDAO.class);

    /**find all record
     *
     * @param connectionDB - connect to data
     * @return list status
     */
    public List<StatusDTO> findAll (Connection connectionDB){

        log.debug("findAll (Connection connectionDB)");

        String sql;
        List<StatusDTO> statusAll = new ArrayList<StatusDTO>();
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null){

            sql = "select status.id, status.statusnamerus, status.statusnameeng, status.updateuserid, status.deleted, status.timestamp " +
                    "from status";

            try{
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()){
                    // todo: !!!
                                        /*
                    statusAll.add(new StatusDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("statusnamerus"), rs.getString("statusnameeng")));
                            */
                }

            } catch (SQLException e) {
                log.error("error of data", e);
            } finally {
                if (rs != null)
                    try{
                        rs.close();
                    }catch (SQLException e){
                        log.error("can't close result set ", e);
                    }
                if (stmt != null)
                    try{
                        stmt.close();
                    }catch (SQLException e){
                        log.error("can't close statement ", e);
                    }
            }
        }else {
            log.error("error connecting data");
        }
        return statusAll;
    }


    /**find one record by id
     *
     * @param connectionDB - connect to data
     * @param idStatus - id status
     * @return status by id
     */
    public StatusDTO findOneById (Connection connectionDB, int idStatus){

        log.debug("findOneById (Connection connectionDB, int idStatus)");

        String sql;
        StatusDTO statusById = null;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null){

            sql = "select status.id, status.statusnamerus, status.statusnameeng, status.updateuserid, status.deleted, status.timestamp " +
                    "from status " +
                    "where status.id = " + idStatus;

            try{
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                if (rs.next()){
                    // todo: !!!
                                        /*
                    statusById = new StatusDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("statusnamerus"), rs.getString("statusnameeng"));
                            */
                }

            } catch (SQLException e) {
                log.error("error of data", e);
            } finally {
                if (rs != null)
                    try{
                        rs.close();
                    }catch (SQLException e){
                        log.error("can't close result set ", e);
                    }
                if (stmt != null)
                    try{
                        stmt.close();
                    }catch (SQLException e){
                        log.error("can't close statement ", e);
                    }
            }
        }else {
            log.error("error connecting data");
        }
        return statusById;
    }


    /**update record
     *
     * @param connectionDB - connect to data
     * @param statusUpdate - status for update
     * @return 0 - update failed, else 1
     */
    public int updateStatus (Connection connectionDB, StatusDTO statusUpdate){

        log.debug("updateStatus (Connection connectionDB, StatusDTO statusUpdate)");

        String sql;
        int resultUpdate = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null){

            sql = "update status set statusnamerus = " + statusUpdate.getNameRus() +
                    ", statusnameeng = " + statusUpdate.getNameEng() +
                    ", updateuserid = " + statusUpdate.getUpdateUser() +
                    ", deleted = " + statusUpdate.getDeleted() +
                    ", timestamp = " + statusUpdate.getCreatedOn() +
                    " when status.id = " + statusUpdate.getId();

            try{
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                resultUpdate =1;

            } catch (SQLException e) {
                log.error("error update data", e);
            } finally {
                if (rs != null)
                    try{
                        rs.close();
                    }catch (SQLException e){
                        log.error("can't close result set ", e);
                    }
                if (stmt != null)
                    try{
                        stmt.close();
                    }catch (SQLException e){
                        log.error("can't close statement ", e);
                    }
            }
        }else {
            log.error("error connecting data");
        }
        return resultUpdate;
    }


    /**insert record
     *
     * @param connectionDB - connect to data
     * @param statusInsert - status for insert
     * @return 0 - insert failed, else 1
     */
    public int insertStatus (Connection connectionDB, StatusDTO statusInsert){

        log.debug("insertStatus (Connection connectionDB, StatusDTO statusInsert)");

        String sql;
        int resultInsert = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null){

            sql = "insert into status (statusnamerus, statusnameeng, updateuserid, deleted, timestamp)" +
                    " values (" + statusInsert.getNameRus() +
                    ", " + statusInsert.getNameEng() +
                    ", " + statusInsert.getUpdateUser() +
                    ", " + statusInsert.getDeleted() +
                    ", " + statusInsert.getCreatedOn() + " )";

            try{
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                resultInsert =1;

            } catch (SQLException e) {
                log.error("error insert data", e);
            } finally {
                if (rs != null)
                    try{
                        rs.close();
                    }catch (SQLException e){
                        log.error("can't close result set ", e);
                    }
                if (stmt != null)
                    try{
                        stmt.close();
                    }catch (SQLException e){
                        log.error("can't close statement ", e);
                    }
            }
        }else {
            log.error("error connecting data");
        }
        return resultInsert;
    }
}
