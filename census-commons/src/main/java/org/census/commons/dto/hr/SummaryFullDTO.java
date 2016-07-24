package org.census.commons.dto.hr;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * hhpilot summary  applicant DTO
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 08.11.12)
 */
public class SummaryFullDTO extends SummaryWithVacancyDTO {

    private ApplicantDTO applicant;

    public SummaryFullDTO() {
    }

    public SummaryFullDTO(int id, int deleted, int updateUser, String timestamp, String phone, String email, String summaryTextRus, String summaryTextEng, MainVacancyDTO mainVacancy, SubVacancyFirstDTO subVacancyFirst, SubVacancySecondDTO subVacancySecond, ApplicantDTO applicant) {
        //super(id, deleted, updateUser, timestamp, phone, email, summaryTextRus, summaryTextEng, mainVacancy, subVacancyFirst, subVacancySecond);
        this.applicant = applicant;
    }

    public ApplicantDTO getApplicant() {
        return applicant;
    }

    public void setApplicant(ApplicantDTO applicant) {
        this.applicant = applicant;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                appendSuper(super.toString()).
                append(" ApplicantDTO = ", applicant.toString()).
                toString();
    }


}
