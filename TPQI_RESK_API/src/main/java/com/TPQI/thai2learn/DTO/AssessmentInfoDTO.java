package com.TPQI.thai2learn.DTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
public class AssessmentInfoDTO {

    private Long id;
    private String examRound;
    private String certifyingBody;
    private String profession;
    private String branch;
    private String occupation;
    private String level;
    private String assessmentTool;
    private String assessmentPlace;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Bangkok")
    private Date examDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Bangkok")
    private Date assessmentDate;

    private String submissionStatus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamRound() {
        return examRound;
    }

    public void setExamRound(String examRound) {
        this.examRound = examRound;
    }

    public String getCertifyingBody() {
        return certifyingBody;
    }

    public void setCertifyingBody(String certifyingBody) {
        this.certifyingBody = certifyingBody;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAssessmentTool() {
        return assessmentTool;
    }

    public void setAssessmentTool(String assessmentTool) {
        this.assessmentTool = assessmentTool;
    }

    public String getAssessmentPlace() {
        return assessmentPlace;
    }

    public void setAssessmentPlace(String assessmentPlace) {
        this.assessmentPlace = assessmentPlace;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public String getSubmissionStatus() {
        return submissionStatus;
    }

    public void setSubmissionStatus(String submissionStatus) {
        this.submissionStatus = submissionStatus;
    }
}