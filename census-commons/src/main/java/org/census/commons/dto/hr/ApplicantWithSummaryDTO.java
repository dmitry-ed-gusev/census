package org.census.commons.dto.hr;

import java.util.List;

/**
 * hhpilot applicant with all summary DTO
 *
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 08.11.12)
 */

public class ApplicantWithSummaryDTO {

    private ApplicantDTO applicant;
    private List<SummaryWithVacancyDTO> allSummary;

    public ApplicantWithSummaryDTO() {
    }

    public ApplicantWithSummaryDTO(ApplicantDTO applicant, List<SummaryWithVacancyDTO> allSummary) {
        this.applicant = applicant;
        this.allSummary = allSummary;
    }

    public ApplicantDTO getApplicant() {
        return applicant;
    }

    public void setApplicant(ApplicantDTO applicant) {
        this.applicant = applicant;
    }

    public List<SummaryWithVacancyDTO> getAllSummary() {
        return allSummary;
    }

    public void setAllSummary(List<SummaryWithVacancyDTO> allSummary) {
        this.allSummary = allSummary;
    }
}
