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

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "occ_level_name")
    private String occLevelName;

    @Column(name = "place")
    private String place;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    
    public Long getExamScheduleId() { return examScheduleId; }
    public void setExamScheduleId(Long examScheduleId) { this.examScheduleId = examScheduleId; }

    public String getTpqiExamNo() { return tpqiExamNo; }
    public void setTpqiExamNo(String tpqiExamNo) { this.tpqiExamNo = tpqiExamNo; }

    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

    public String getOccLevelName() { return occLevelName; }
    public void setOccLevelName(String occLevelName) { this.occLevelName = occLevelName; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}