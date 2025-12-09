package com.TPQI.thai2learn.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ProxyAssessmentDTO {

    @NotNull(message = "Applicant ID is required")
    private Long applicantId;

    @NotBlank(message = "Original examiner code is required")
    private String originalExaminerCode;

    @NotBlank(message = "Assessment result status is required")
    private String resultStatus;

    private String comments;

    private List<String> passedCompetencyCodes;


    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public String getOriginalExaminerCode() { return originalExaminerCode; }
    public void setOriginalExaminerCode(String originalExaminerCode) { this.originalExaminerCode = originalExaminerCode; }
    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public List<String> getPassedCompetencyCodes() { return passedCompetencyCodes; }
    public void setPassedCompetencyCodes(List<String> passedCompetencyCodes) { this.passedCompetencyCodes = passedCompetencyCodes; }
}