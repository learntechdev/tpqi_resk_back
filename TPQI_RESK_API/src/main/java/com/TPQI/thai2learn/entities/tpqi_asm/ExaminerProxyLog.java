package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "examiner_proxy_log")
public class ExaminerProxyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Column(name = "applicant_id", nullable = false)
    private Long applicantId;

    @Column(name = "original_examiner_code", nullable = false)
    private String originalExaminerCode;

    @Column(name = "proxy_examiner_code", nullable = false)
    private String proxyExaminerCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }
    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public String getOriginalExaminerCode() { return originalExaminerCode; }
    public void setOriginalExaminerCode(String originalExaminerCode) { this.originalExaminerCode = originalExaminerCode; }
    public String getProxyExaminerCode() { return proxyExaminerCode; }
    public void setProxyExaminerCode(String proxyExaminerCode) { this.proxyExaminerCode = proxyExaminerCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}