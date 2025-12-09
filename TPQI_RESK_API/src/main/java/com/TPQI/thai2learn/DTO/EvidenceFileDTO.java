package com.TPQI.thai2learn.DTO;

import java.util.List;

public class EvidenceFileDTO {
    private Long id;
    private String originalFilename;
    private String filePath;
    private List<String> evidenceTypes;
    private String description;

    private boolean isLinked;
    private List<String> linkedCompetencyCodes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public List<String> getEvidenceTypes() { return evidenceTypes; }
    public void setEvidenceTypes(List<String> evidenceTypes) { this.evidenceTypes = evidenceTypes; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isLinked() { return isLinked; }
    public void setLinked(boolean linked) { isLinked = linked; }

    public List<String> getLinkedCompetencyCodes() { return linkedCompetencyCodes; }
    public void setLinkedCompetencyCodes(List<String> linkedCompetencyCodes) { this.linkedCompetencyCodes = linkedCompetencyCodes; }
}
