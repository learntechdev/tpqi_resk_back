package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;

@Entity
@Table(name = "assessment_submission_details")
public class AssessmentSubmissionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_applicant_id", nullable = false, unique = true)
    private Long assessmentApplicantId;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "submission_status")
    private String submissionStatus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAssessmentApplicantId() { return assessmentApplicantId; }
    public void setAssessmentApplicantId(Long assessmentApplicantId) { this.assessmentApplicantId = assessmentApplicantId; }
    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public String getSubmissionStatus() { return submissionStatus; }
    public void setSubmissionStatus(String submissionStatus) { this.submissionStatus = submissionStatus; }
}