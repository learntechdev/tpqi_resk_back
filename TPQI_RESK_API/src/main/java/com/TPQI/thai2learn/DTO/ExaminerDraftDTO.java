package com.TPQI.thai2learn.DTO;

import java.util.List;

public class ExaminerDraftDTO {
    private Long applicantId;
    private String comments;
    private List<EvidenceLinkDTO> evidenceLinks;
    private String resultStatus;
    private List<RequestedEvidenceDTO> requestedEvidences;

    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public List<EvidenceLinkDTO> getEvidenceLinks() { return evidenceLinks; }
    public void setEvidenceLinks(List<EvidenceLinkDTO> evidenceLinks) { this.evidenceLinks = evidenceLinks; }
    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
    public List<RequestedEvidenceDTO> getRequestedEvidences() { return requestedEvidences; }
    public void setRequestedEvidences(List<RequestedEvidenceDTO> requestedEvidences) { this.requestedEvidences = requestedEvidences; }
}