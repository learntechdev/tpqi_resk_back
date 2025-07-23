package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exam_schedule")
public class ExamSchedule {

    @Id
    @Column(name = "exam_schedule_id")
    private Long examScheduleId;

    @Column(name = "tpqi_exam_no")
    private String tpqiExamNo;

    @Column(name = "org_name")
    private String orgName;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;


    public Long getExamScheduleId() {
        return examScheduleId;
    }

    public void setExamScheduleId(Long examScheduleId) {
        this.examScheduleId = examScheduleId;
    }

    public String getTpqiExamNo() {
        return tpqiExamNo;
    }

    public void setTpqiExamNo(String tpqiExamNo) {
        this.tpqiExamNo = tpqiExamNo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}