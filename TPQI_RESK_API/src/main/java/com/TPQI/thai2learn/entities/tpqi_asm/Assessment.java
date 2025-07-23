package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "assessment")
public class Assessment {

    @Id
    @Column(name = "assessment_id")
    private Long assessmentId;

    @Column(name = "app_id")
    private String appId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "assessment_date")
    private Date assessmentDate;

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }
}