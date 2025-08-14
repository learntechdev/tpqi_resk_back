package com.TPQI.thai2learn.DTO;

public class ExaminerInfoDTO {
    private String examinerCode;
    private String fullName;

    public ExaminerInfoDTO(String examinerCode, String fullName) {
        this.examinerCode = examinerCode;
        this.fullName = fullName;
    }

    public String getExaminerCode() { return examinerCode; }
    public void setExaminerCode(String examinerCode) { this.examinerCode = examinerCode; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}