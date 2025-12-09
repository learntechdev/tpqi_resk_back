package com.TPQI.thai2learn.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AssessmentCancellationDTO {

    @NotNull(message = "Applicant ID is required")
    private Long applicantId;

    @NotBlank(message = "Reason for cancellation is required")
    private String reason;

    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}