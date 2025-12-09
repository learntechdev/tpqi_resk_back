package com.TPQI.thai2learn.DTO;

import java.util.Date;

public class CbExamRoundDTO {
    private Long examScheduleId;      
    
    private String tpqiExamNo;        

    private String profession;
    private String branch;
    private String occupation;
    private String level;

    private String assessmentTool;

    private String assessmentPlace;

    private Date examDate;

    private Date assessmentDate;

    private Long applicantCount;

    public CbExamRoundDTO(Long examScheduleId, String tpqiExamNo, String profession, String branch, String occupation, String level, String assessmentTool, String assessmentPlace, Date examDate, Date assessmentDate, Long applicantCount) {
        this.examScheduleId = examScheduleId;
        this.tpqiExamNo = tpqiExamNo;
        this.profession = profession;
        this.branch = branch;
        this.occupation = occupation;
        this.level = level;
        this.assessmentTool = assessmentTool;
        this.assessmentPlace = assessmentPlace;
        this.examDate = examDate;
        this.assessmentDate = assessmentDate;
        this.applicantCount = applicantCount;
    }

    public Long getExamScheduleId() { return examScheduleId; }
    public void setExamScheduleId(Long examScheduleId) { this.examScheduleId = examScheduleId; }
    public String getTpqiExamNo() { return tpqiExamNo; }
    public void setTpqiExamNo(String tpqiExamNo) { this.tpqiExamNo = tpqiExamNo; }
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getAssessmentTool() { return assessmentTool; }
    public void setAssessmentTool(String assessmentTool) { this.assessmentTool = assessmentTool; }
    public String getAssessmentPlace() { return assessmentPlace; }
    public void setAssessmentPlace(String assessmentPlace) { this.assessmentPlace = assessmentPlace; }
    public Date getExamDate() { return examDate; }
    public void setExamDate(Date examDate) { this.examDate = examDate; }
    public Date getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(Date assessmentDate) { this.assessmentDate = assessmentDate; }
    public Long getApplicantCount() { return applicantCount; }
    public void setApplicantCount(Long applicantCount) { this.applicantCount = applicantCount; }
}