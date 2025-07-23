package com.TPQI.thai2learn.DTO;

import java.util.List;

public class SubmissionRequestDTO {
    private Long assessmentApplicantId;
    private Integer experienceYears;
    private Boolean hasPriorCertificate;
    private List<PriorCertificateDTO> priorCertificates;
    private String submissionStatus;
    private List<EvidenceLinkDTO> evidenceLinks;

    private List<EvidenceDetailDTO> evidenceDetails;

    public Long getAssessmentApplicantId() { return assessmentApplicantId; }
    public void setAssessmentApplicantId(Long assessmentApplicantId) { this.assessmentApplicantId = assessmentApplicantId; }
    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public Boolean getHasPriorCertificate() { return hasPriorCertificate; }
    public void setHasPriorCertificate(Boolean hasPriorCertificate) { this.hasPriorCertificate = hasPriorCertificate; }
    public List<PriorCertificateDTO> getPriorCertificates() { return priorCertificates; }
    public void setPriorCertificates(List<PriorCertificateDTO> priorCertificates) { this.priorCertificates = priorCertificates; }
    public String getSubmissionStatus() { return submissionStatus; }
    public void setSubmissionStatus(String submissionStatus) { this.submissionStatus = submissionStatus; }
    public List<EvidenceLinkDTO> getEvidenceLinks() { return evidenceLinks; }
    public void setEvidenceLinks(List<EvidenceLinkDTO> evidenceLinks) { this.evidenceLinks = evidenceLinks; }

    public List<EvidenceDetailDTO> getEvidenceDetails() { return evidenceDetails; }
    public void setEvidenceDetails(List<EvidenceDetailDTO> evidenceDetails) { this.evidenceDetails = evidenceDetails; }
}