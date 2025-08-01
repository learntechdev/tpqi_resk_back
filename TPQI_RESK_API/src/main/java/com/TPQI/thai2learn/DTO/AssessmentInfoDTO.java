package com.TPQI.thai2learn.DTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
//สำหรับแสดงในหน้าแรกของผู้รับการประเมิน
public class AssessmentInfoDTO {

    private Long id;                    // PK
    private String examRound;           // รอบสอบ
    private String certifyingBody;      // องค์กรรับรอง
    private String profession;          // คุณวุฒิวิชาชีพ
    private String branch;              // สาขา
    private String occupation;          // อาชีพ
    private String level;               // ระดับ
    private String assessmentTool;      // เครื่องมือประเมิน
    private String assessmentPlace;     // สถานที่จัดสอบ
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Bangkok")
    private Date examDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Bangkok")
    private Date assessmentDate;
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
}