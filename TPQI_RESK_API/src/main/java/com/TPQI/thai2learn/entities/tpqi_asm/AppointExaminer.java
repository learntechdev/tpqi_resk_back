package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;

@Entity
@Table(name = "appoint_examiner")
public class AppointExaminer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "examiner_code")
    private String examinerCode;

    @Column(name = "examiner_full_name")
    private String examinerFullName;

    @Column(name = "tpqi_exam_no")
    private String tpqiExamNo;


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getExaminerCode() { return examinerCode; }
    public void setExaminerCode(String examinerCode) { this.examinerCode = examinerCode; }
    public String getExaminerFullName() { return examinerFullName; }
    public void setExaminerFullName(String examinerFullName) { this.examinerFullName = examinerFullName; }
    public String getTpqiExamNo() { return tpqiExamNo; }
    public void setTpqiExamNo(String tpqiExamNo) { this.tpqiExamNo = tpqiExamNo; }
}