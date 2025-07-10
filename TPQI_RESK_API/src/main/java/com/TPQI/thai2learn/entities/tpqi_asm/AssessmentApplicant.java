package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;

@Entity
@Table(name = "assessment_applicant")
public class AssessmentApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "submission_status")
    private String submissionStatus;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public String getSubmissionStatus() { return submissionStatus; }
    public void setSubmissionStatus(String submissionStatus) { this.submissionStatus = submissionStatus; }
}