package com.TPQI.thai2learn.DTO;

import java.util.List;

public class AssessmentFilterOptionsDTO {

    private List<String> professions;
    private List<String> branches;
    private List<String> occupations;
    private List<String> levels;
    private List<String> assessmentTools;
    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }


    public List<String> getOccupations() {
        return occupations;
    }

    public void setOccupations(List<String> occupations) {
        this.occupations = occupations;
    }

    
    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public List<String> getAssessmentTools() {
        return assessmentTools;
    }

    public void setAssessmentTools(List<String> assessmentTools) {
        this.assessmentTools = assessmentTools;
    }

    public List<String> getProfessions() {
        return professions;
    }

    public void setProfessions(List<String> professions) {
        this.professions = professions;
    }
}