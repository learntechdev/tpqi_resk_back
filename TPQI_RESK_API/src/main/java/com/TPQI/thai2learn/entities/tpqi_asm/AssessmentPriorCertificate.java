package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;

@Entity
@Table(name = "assessment_prior_certificates")
public class AssessmentPriorCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_applicant_id", nullable = false)
    private Long assessmentApplicantId;

    @Column(name = "qualification_id", nullable = false)
    private Long qualificationId;

    @Column(name = "evidence_file_id", nullable = false)
    private Long evidenceFileId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAssessmentApplicantId() { return assessmentApplicantId; }
    public void setAssessmentApplicantId(Long assessmentApplicantId) { this.assessmentApplicantId = assessmentApplicantId; }
    public Long getQualificationId() { return qualificationId; }
    public void setQualificationId(Long qualificationId) { this.qualificationId = qualificationId; }
    public Long getEvidenceFileId() { return evidenceFileId; }
    public void setEvidenceFileId(Long evidenceFileId) { this.evidenceFileId = evidenceFileId; }
}