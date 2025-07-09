package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_evidence_files")
public class AssessmentEvidenceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_applicant_id", nullable = false)
    private Long assessmentApplicantId; // ID primary_key จากตาราง assessment_applicant

    @Column(name = "file_path", nullable = false)
    private String filePath; 

    @Column(name = "original_filename")
    private String originalFilename; 

    @Column(name = "evidence_type", nullable = false)
    private String evidenceType; 

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

    public Long getAssessmentApplicantId() {
        return assessmentApplicantId;
    }

    public void setAssessmentApplicantId(Long assessmentApplicantId) {
        this.assessmentApplicantId = assessmentApplicantId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}