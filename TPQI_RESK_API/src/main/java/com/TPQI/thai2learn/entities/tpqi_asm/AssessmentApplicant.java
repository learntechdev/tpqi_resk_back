package com.TPQI.thai2learn.entities.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.types.AssessmentStatus;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "assessment_applicant")
public class AssessmentApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_schedule_id")
    private String examScheduleId;

    @Column(name = "asm_tool_type")
    private String asmToolType;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "citizen_id")
    private String citizenId;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "initial_name")
    private String initialName;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "occ_level_id")
    private Integer occLevelId;

    @Column(name = "occ_level_name")
    private String occLevelName;

    @Temporal(TemporalType.DATE)
    @Column(name = "schedule_exam_date")
    private Date scheduleExamDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "assessment_date")
    private Date assessmentDate;

    @Column(name = "assessment_status")
    private AssessmentStatus assessmentStatus;

    @Column(name = "confirm_ass_status")
    private String confirmAssStatus;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamScheduleId() {
        return examScheduleId;
    }

    public void setExamScheduleId(String examScheduleId) {
        this.examScheduleId = examScheduleId;
    }

    public String getAsmToolType() {
        return asmToolType;
    }

    public void setAsmToolType(String asmToolType) {
        this.asmToolType = asmToolType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getInitialName() {
        return initialName;
    }

    public void setInitialName(String initialName) {
        this.initialName = initialName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getOccLevelId() {
        return occLevelId;
    }

    public void setOccLevelId(Integer occLevelId) {
        this.occLevelId = occLevelId;
    }

    public String getOccLevelName() {
        return occLevelName;
    }

    public void setOccLevelName(String occLevelName) {
        this.occLevelName = occLevelName;
    }

    public Date getScheduleExamDate() {
        return scheduleExamDate;
    }

    public void setScheduleExamDate(Date scheduleExamDate) {
        this.scheduleExamDate = scheduleExamDate;
    }

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public AssessmentStatus getAssessmentStatus() {
        return assessmentStatus;
    }

    public void setAssessmentStatus(AssessmentStatus assessmentStatus) {
        this.assessmentStatus = assessmentStatus;
    }

    public String getConfirmAssStatus() {
        return confirmAssStatus;
    }

    public void setConfirmAssStatus(String confirmAssStatus) {
        this.confirmAssStatus = confirmAssStatus;
    }
}