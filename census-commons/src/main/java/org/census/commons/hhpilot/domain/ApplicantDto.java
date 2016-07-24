package org.census.commons.hhpilot.domain;

/**
 * hhpilot domain object -> applicant.
 * @author Gusev Dmitry (GusevD)
 * @version 1.0 (DATE: 21.10.12)
 */

public class ApplicantDto {

    private int     id;
    private String  name;         // name, example: Dmitry
    private String  secondName;   // second name, example: Eduardovitch
    private String  surname;      // surname, example: Gusev
    private String  nameEng;
    private String  secondNameEng;
    private String  surnameEng;
    private boolean isMale;
    private String  email;
    private String  phone;
    private String  photo;
    private String  comments;
    private String  inputDate;
    private int     status;
    private String  timestamp;

    public ApplicantDto() {
    }

    public ApplicantDto(String name, String secondName, String surname, boolean male, String comments) {
        this.name       = name;
        this.secondName = secondName;
        this.surname    = surname;
        this.isMale     = male;
        this.comments   = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getInputDate() {
        return inputDate;
    }

    public void setInputDate(String inputDate) {
        this.inputDate = inputDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public String getSecondNameEng() {
        return secondNameEng;
    }

    public void setSecondNameEng(String secondNameEng) {
        this.secondNameEng = secondNameEng;
    }

    public String getSurnameEng() {
        return surnameEng;
    }

    public void setSurnameEng(String surnameEng) {
        this.surnameEng = surnameEng;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}