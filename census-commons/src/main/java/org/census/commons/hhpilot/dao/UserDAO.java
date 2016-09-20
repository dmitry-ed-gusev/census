package org.census.commons.hhpilot.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.hr.UsersDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * hhpilot user DAO
 *
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 09.11.12)
 */
public class UserDAO {

    private Log log = LogFactory.getLog(UserDAO.class);

    /**
     * find all user
     * @param connectionDB - connect to data
     * @return list user
     */
    public List<UsersDTO> findAll (Connection connectionDB){

        log.debug("findAll (Connection connectionDB)");

        String sql;
        List<UsersDTO> userAll = new ArrayList<UsersDTO>();
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null){

            sql = "select users.id, users.fio, users.login, users.pass, users.datevalidpass, users.updateuserid, users.deleted, users.timestamp " +
                    " from users";

            try{
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()){
                    userAll.add(new UsersDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("fio"), rs.getString("login"), rs.getString("pass"), rs.getDate("datevalidpass")));
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
        return userAll;
    }

    /**
     * find one user
     * @param connectionDB - connect to data
     * @param userId - id user
     * @return  user
     */
    public UsersDTO findOneById (Connection connectionDB, int userId){

        log.debug("findOneById (Connection connectionDB, int userId)");

        String sql;
        UsersDTO user = null;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null){

            sql = "select users.id, users.fio, users.login, users.pass, users.datevalidpass, users.updateuserid, users.deleted, users.timestamp " +
                    " from users " +
                    " where users.id = " + userId;

            try{
                stmt = connectionDB.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()){
                    user = new UsersDTO(rs.getInt("id"), rs.getInt("deleted"), rs.getInt("updateuserid"), rs.getString("timestamp"),
                            rs.getString("fio"), rs.getString("login"), rs.getString("pass"), rs.getDate("datevalidpass"));
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
        return user;
    }

    /**update record
     *
     * @param connectionDB      - connect to data
     * @param usersUpdate - user for update
     * @return 0 - update failed, else 1
     */
    public int updateUsers (Connection connectionDB, UsersDTO usersUpdate){

        log.debug("updateUsers (Connection connectionDB, UsersDTO usersUpdate)");

        String sql;
        int resultUpdate = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "update users set fio =  " + usersUpdate.getFIO() +
                    ", login = " + usersUpdate.getLogin() +
                    ", pass = " + usersUpdate.getPass() +
                    ", datevalidpass = " + usersUpdate.getDateValidPass() +
                    ", updateuserid = " + usersUpdate.getUpdateUser() +
                    ", deleted = " + usersUpdate.getDeleted() +
                    ", timestamp = " + usersUpdate.getCreatedOn() +
                    " when users.id = " + usersUpdate.getId();

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

    /**insert record
     *
     * @param connectionDB      - connect to data
     * @param usersInsert - user for insert
     * @return 0 - insert failed, else 1
     */
    public int insertUsers (Connection connectionDB, UsersDTO usersInsert){

        log.debug("insertUsers (Connection connectionDB, UsersDTO usersInsert)");

        String sql;
        int resultUpdate = 0;
        Statement stmt = null;
        ResultSet rs = null;

        if (connectionDB != null) {

            sql = "insert into users (fio, login, pass, datevalidpass, updateuserid, deleted, timestamp) " +
                    " values (" + usersInsert.getFIO() +
                    ", " + usersInsert.getLogin() +
                    ", " + usersInsert.getPass() +
                    ", " + usersInsert.getDateValidPass() +
                    ", " + usersInsert.getUpdateUser() +
                    ", " + usersInsert.getDeleted() +
                    ", " + usersInsert.getCreatedOn() + ")";

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
