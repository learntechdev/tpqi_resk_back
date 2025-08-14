package com.TPQI.thai2learn.DTO;

import java.util.List;

public class ExaminerFilterOptionsDTO {

    private List<String> qualifications;
    private List<String> levels;
    private List<String> assessmentTools;

    public List<String> getQualifications() { return qualifications; }
    public void setQualifications(List<String> qualifications) { this.qualifications = qualifications; }
    public List<String> getLevels() { return levels; }
    public void setLevels(List<String> levels) { this.levels = levels; }
    public List<String> getAssessmentTools() { return assessmentTools; }
    public void setAssessmentTools(List<String> assessmentTools) { this.assessmentTools = assessmentTools; }
}