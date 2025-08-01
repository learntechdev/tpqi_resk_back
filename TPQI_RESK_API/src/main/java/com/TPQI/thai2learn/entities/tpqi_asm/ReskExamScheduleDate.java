package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "resk_exam_schedule_dates")
public class ReskExamScheduleDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tpqi_exam_no", unique = true, nullable = false)
    private String tpqiExamNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "actual_exam_date")
    private Date actualExamDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "actual_assessment_date")
    private Date actualAssessmentDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTpqiExamNo() { return tpqiExamNo; }
    public void setTpqiExamNo(String tpqiExamNo) { this.tpqiExamNo = tpqiExamNo; }
    public Date getActualExamDate() { return actualExamDate; }
    public void setActualExamDate(Date actualExamDate) { this.actualExamDate = actualExamDate; }
    public Date getActualAssessmentDate() { return actualAssessmentDate; }
    public void setActualAssessmentDate(Date actualAssessmentDate) { this.actualAssessmentDate = actualAssessmentDate; }
}