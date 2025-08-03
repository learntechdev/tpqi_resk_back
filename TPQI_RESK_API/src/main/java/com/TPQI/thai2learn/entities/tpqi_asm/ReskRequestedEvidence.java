package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "resk_requested_evidences")
public class ReskRequestedEvidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_applicant_id", nullable = false)
    private Long assessmentApplicantId;

    @Column(name = "uoc_code", nullable = false)
    private String uocCode;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "requested_by_examiner_code")
    private String requestedByExaminerCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "requested_at", nullable = false)
    private Date requestedAt;

    @Column(name = "is_fulfilled", nullable = false)
    private boolean isFulfilled = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAssessmentApplicantId() { return assessmentApplicantId; }
    public void setAssessmentApplicantId(Long assessmentApplicantId) { this.assessmentApplicantId = assessmentApplicantId; }

    public String getUocCode() { return uocCode; }
    public void setUocCode(String uocCode) { this.uocCode = uocCode; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getRequestedByExaminerCode() { return requestedByExaminerCode; }
    public void setRequestedByExaminerCode(String requestedByExaminerCode) { this.requestedByExaminerCode = requestedByExaminerCode; }

    public Date getRequestedAt() { return requestedAt; }
    public void setRequestedAt(Date requestedAt) { this.requestedAt = requestedAt; }

    public boolean isFulfilled() { return isFulfilled; }
    public void setFulfilled(boolean fulfilled) { isFulfilled = fulfilled; }
}