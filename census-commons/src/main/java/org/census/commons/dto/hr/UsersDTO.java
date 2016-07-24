package org.census.commons.dto.hr;

import org.census.commons.dto.AbstractEntityDto;

import java.util.Date;

/**
 * hhpilot user system HHPilot DTO
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 26.10.12)
 */
public class UsersDTO extends AbstractEntityDto {

    private String FIO;
    private String login;
    private String pass;
    private Date dateValidPass;

    public UsersDTO() {
    }

    public UsersDTO(int id, int deleted, int updateUser, String timestamp, String FIO, String login, String pass, Date dateValidPass) {
        //super(id, deleted, updateUser, timestamp);

        this.setId(id);
        this.setDeleted(deleted);
        this.setUpdateUser(updateUser);
        //this.setTimestamp(timestamp);
        this.FIO = FIO;
        this.login = login;
        this.pass = pass;
        this.dateValidPass = dateValidPass;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Date getDateValidPass() {
        return dateValidPass;
    }

    public void setDateValidPass(Date dateValidPass) {
        this.dateValidPass = dateValidPass;
    }
}
