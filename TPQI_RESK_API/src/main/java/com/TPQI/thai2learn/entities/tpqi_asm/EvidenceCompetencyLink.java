package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidence_competency_link")
public class EvidenceCompetencyLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evidence_file_id", nullable = false)
    private Long evidenceFileId;

    @Column(name = "competency_code", nullable = false)
    private String competencyCode;

    @Column(name = "assessment_applicant_id", nullable = false)
    private Long assessmentApplicantId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEvidenceFileId() {
        return evidenceFileId;
    }

    public void setEvidenceFileId(Long evidenceFileId) {
        this.evidenceFileId = evidenceFileId;
    }

    public String getCompetencyCode() {
        return competencyCode;
    }

    public void setCompetencyCode(String competencyCode) {
        this.competencyCode = competencyCode;
    }

    public Long getAssessmentApplicantId() {
        return assessmentApplicantId;
    }

    public void setAssessmentApplicantId(Long assessmentApplicantId) {
        this.assessmentApplicantId = assessmentApplicantId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}