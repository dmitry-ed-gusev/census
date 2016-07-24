package org.census.commons.dto.hr;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.census.commons.dto.AbstractEntityDto;

import java.util.Date;

/**
 * hhpilot applicant DTO
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 24.10.12)
 * @deprecated
 */

public class ApplicantDTO extends AbstractEntityDto {

    private String fioRus;
    private String fioEng;
    //true = men
    private boolean sex;
    private String comments;
    private Date dateInput;
    private String photo;

    public ApplicantDTO() {
    }

    /*
    public ApplicantDTO(int id, int deleted, int updateUser, String timestamp, String fioRus, String fioEng, boolean sex, String comments, Date dateInput, String photo) {
        super(id, deleted, updateUser, timestamp);
        this.fioRus = fioRus;
        this.fioEng = fioEng;
        this.sex = sex;
        this.comments = comments;
        this.dateInput = dateInput;
        this.photo = photo;
    }
    */

    public String getFioRus() {
        return fioRus;
    }

    public void setFioRus(String fioRus) {
        this.fioRus = fioRus;
    }

    public String getFioEng() {
        return fioEng;
    }

    public void setFioEng(String fioEng) {
        this.fioEng = fioEng;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getDateInput() {
        return dateInput;
    }

    public void setDateInput(Date dateInput) {
        this.dateInput = dateInput;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                appendSuper(super.toString()).
                append(" fioRus = ", fioRus).
                append(" fioEng = ", fioEng).
                append(" sex = ", sex == true ? "men" : "women").
                append(" comments = ", comments).
                append(" dateInput = ", dateInput.toString()).
                append(" photo = ", photo).
                toString();
    }

}
