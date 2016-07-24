package org.census.commons.dto.hr;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * hhpilot summary base with vacancy applicant DTO
 *
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 08.11.12)
 */
public class SummaryWithVacancyDTO extends SummaryDTO {

    private MainVacancyDTO mainVacancy;
    private SubVacancyFirstDTO subVacancyFirst;
    private SubVacancySecondDTO subVacancySecond;

    public SummaryWithVacancyDTO() {
    }

    /*
    public SummaryWithVacancyDTO(int id, int deleted, int updateUser, String timestamp, String phone, String email, String summaryTextRus, String summaryTextEng, MainVacancyDTO mainVacancy, SubVacancyFirstDTO subVacancyFirst, SubVacancySecondDTO subVacancySecond) {
        super(id, deleted, updateUser, timestamp, phone, email, summaryTextRus, summaryTextEng);
        this.mainVacancy = mainVacancy;
        this.subVacancyFirst = subVacancyFirst;
        this.subVacancySecond = subVacancySecond;
    }
    */

    public MainVacancyDTO getMainVacancy() {
        return mainVacancy;
    }

    public void setMainVacancy(MainVacancyDTO mainVacancy) {
        this.mainVacancy = mainVacancy;
    }

    public SubVacancyFirstDTO getSubVacancyFirst() {
        return subVacancyFirst;
    }

    public void setSubVacancyFirst(SubVacancyFirstDTO subVacancyFirst) {
        this.subVacancyFirst = subVacancyFirst;
    }

    public SubVacancySecondDTO getSubVacancySecond() {
        return subVacancySecond;
    }

    public void setSubVacancySecond(SubVacancySecondDTO subVacancySecond) {
        this.subVacancySecond = subVacancySecond;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                appendSuper(super.toString()).
                append(" mainVacancy = ", mainVacancy.toString()).
                append(" subVacancy1 = ", subVacancyFirst.toString()).
                append(" subVacancy2 = ", subVacancySecond.toString()).
                toString();
    }
}