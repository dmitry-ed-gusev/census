package org.census.commons.dto.hr;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.census.commons.dto.AbstractEntity;

/**
 * hhpilot summary base applicant DTO
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 24.10.12)
 */

public class SummaryDTO extends AbstractEntity {

    private String phone;
    private String email;
    private String summaryTextRus;
    private String summaryTextEng;

    public SummaryDTO() {
    }

    /*
    public SummaryDTO(int id, int deleted, int updateUser, String timestamp, String phone, String email, String summaryTextRus, String summaryTextEng) {
        super(id, deleted, updateUser, timestamp);
        this.phone = phone;
        this.email = email;
        this.summaryTextRus = summaryTextRus;
        this.summaryTextEng = summaryTextEng;
    }
    */

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSummaryTextRus() {
        return summaryTextRus;
    }

    public void setSummaryTextRus(String summaryTextRus) {
        this.summaryTextRus = summaryTextRus;
    }

    public String getSummaryTextEng() {
        return summaryTextEng;
    }

    public void setSummaryTextEng(String summaryTextEng) {
        this.summaryTextEng = summaryTextEng;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                appendSuper(super.toString()).
                append(" phone = ", phone).
                append(" email = ", email).
                append(" textRus = ", summaryTextRus).
                append(" textEng = ", summaryTextEng).
                toString();
    }
}
