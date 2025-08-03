package com.TPQI.thai2learn.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ExaminerAssessmentDTO {

    @NotNull(message = "Applicant ID is required")
    private Long applicantId;

    @NotBlank(message = "Assessment result status is required")
    private String resultStatus;

    private String comments;

    @Valid
    private List<RequestedEvidenceDTO> requestedEvidences;

    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public List<RequestedEvidenceDTO> getRequestedEvidences() { return requestedEvidences; }
    public void setRequestedEvidences(List<RequestedEvidenceDTO> requestedEvidences) { this.requestedEvidences = requestedEvidences; }
}