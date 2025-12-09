package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "assessment")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_id")
    private Long assessmentId;

    @Column(name = "exam_schedule_id", nullable = false)
    private String examScheduleId;

    @Column(name = "app_id", nullable = false, unique = true)
    private String appId;

    @Column(name = "citizen_id", nullable = false)
    private String citizenId;

    @Column(name = "tool_type", nullable = false)
    private String toolType;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @Column(name = "full_score", nullable = false)
    private Integer fullScore;

    @Column(name = "exam_percent_score", nullable = false)
    private BigDecimal examPercentScore;

    @Column(name = "exam_result", nullable = false)
    private String examResult;

    @Column(name = "recomment", columnDefinition = "TEXT")
    private String recomment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "assessment_date", nullable = false)
    private Date assessmentDate;

    @Column(name = "type", nullable = false)
    private String type;

    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }
    public String getExamScheduleId() { return examScheduleId; }
    public void setExamScheduleId(String examScheduleId) { this.examScheduleId = examScheduleId; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getCitizenId() { return citizenId; }
    public void setCitizenId(String citizenId) { this.citizenId = citizenId; }
    public String getToolType() { return toolType; }
    public void setToolType(String toolType) { this.toolType = toolType; }
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    public Integer getFullScore() { return fullScore; }
    public void setFullScore(Integer fullScore) { this.fullScore = fullScore; }
    public BigDecimal getExamPercentScore() { return examPercentScore; }
    public void setExamPercentScore(BigDecimal examPercentScore) { this.examPercentScore = examPercentScore; }
    public String getExamResult() { return examResult; }
    public void setExamResult(String examResult) { this.examResult = examResult; }
    public String getRecomment() { return recomment; }
    public void setRecomment(String recomment) { this.recomment = recomment; }
    public Date getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(Date assessmentDate) { this.assessmentDate = assessmentDate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}