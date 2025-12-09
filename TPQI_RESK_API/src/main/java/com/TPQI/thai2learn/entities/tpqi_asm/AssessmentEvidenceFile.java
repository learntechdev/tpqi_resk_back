package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assessment_evidence_files")
public class AssessmentEvidenceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_applicant_id", nullable = false)
    private Long assessmentApplicantId;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "description")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "assessment_evidence_types", joinColumns = @JoinColumn(name = "evidence_file_id"))
    @Column(name = "evidence_type", nullable = false)
    private List<String> evidenceTypes;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getEvidenceTypes() {
        return evidenceTypes;
    }

    public void setEvidenceTypes(List<String> evidenceTypes) {
        this.evidenceTypes = evidenceTypes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}