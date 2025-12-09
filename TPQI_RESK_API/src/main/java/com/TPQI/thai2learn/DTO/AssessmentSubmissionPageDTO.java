package com.TPQI.thai2learn.DTO;

import java.util.List;

public class AssessmentSubmissionPageDTO {

    private Long assessmentApplicantId;
    private String applicationCode;
    private String fullNameThai;
    private String professionName;
    private Integer experienceYears;
    private Boolean hasPriorCertificate;
    private List<EvidenceFileDTO> evidenceFiles;
    private List<PriorCertificateDTO> savedPriorCertificates;
    private List<UocDTO> competencyTree;
    private List<RelatedQualificationDTO> relatedQualifications;
    private List<UnlinkedUocDTO> unlinkedCompetencies;
    private List<RequestedEvidenceInfoDTO> requestedEvidences;
    private List<EvidenceLinkDTO> examinerEvidenceLinks;
    private String examinerComments;
    private String examinerResultStatus;

    
    public List<UnlinkedUocDTO> getUnlinkedCompetencies() { return unlinkedCompetencies; }
    public void setUnlinkedCompetencies(List<UnlinkedUocDTO> unlinkedCompetencies) { this.unlinkedCompetencies = unlinkedCompetencies; }

    public Long getAssessmentApplicantId() {
        return assessmentApplicantId;
    }

    public void setAssessmentApplicantId(Long assessmentApplicantId) {
        this.assessmentApplicantId = assessmentApplicantId;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getFullNameThai() {
        return fullNameThai;
    }

    public void setFullNameThai(String fullNameThai) {
        this.fullNameThai = fullNameThai;
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Boolean getHasPriorCertificate() {
        return hasPriorCertificate;
    }

    public void setHasPriorCertificate(Boolean hasPriorCertificate) {
        this.hasPriorCertificate = hasPriorCertificate;
    }

    public List<EvidenceFileDTO> getEvidenceFiles() {
        return evidenceFiles;
    }

    public void setEvidenceFiles(List<EvidenceFileDTO> evidenceFiles) {
        this.evidenceFiles = evidenceFiles;
    }

    public List<PriorCertificateDTO> getSavedPriorCertificates() {
        return savedPriorCertificates;
    }

    public void setSavedPriorCertificates(List<PriorCertificateDTO> savedPriorCertificates) {
        this.savedPriorCertificates = savedPriorCertificates;
    }

    public List<UocDTO> getCompetencyTree() {
        return competencyTree;
    }

    public void setCompetencyTree(List<UocDTO> competencyTree) {
        this.competencyTree = competencyTree;
    }

    public List<RelatedQualificationDTO> getRelatedQualifications() {
        return relatedQualifications;
    }

    public void setRelatedQualifications(List<RelatedQualificationDTO> relatedQualifications) {
        this.relatedQualifications = relatedQualifications;
    }

    public List<RequestedEvidenceInfoDTO> getRequestedEvidences() { return requestedEvidences; }
    public void setRequestedEvidences(List<RequestedEvidenceInfoDTO> requestedEvidences) { this.requestedEvidences = requestedEvidences; }

    public List<EvidenceLinkDTO> getExaminerEvidenceLinks() {
        return examinerEvidenceLinks;
    }

    public void setExaminerEvidenceLinks(List<EvidenceLinkDTO> examinerEvidenceLinks) {
        this.examinerEvidenceLinks = examinerEvidenceLinks;
    }

    public String getExaminerComments() {
        return examinerComments;
    }

    public void setExaminerComments(String examinerComments) {
        this.examinerComments = examinerComments;
    }

    public String getExaminerResultStatus() { return examinerResultStatus; }
    public void setExaminerResultStatus(String examinerResultStatus) { this.examinerResultStatus = examinerResultStatus; }
}