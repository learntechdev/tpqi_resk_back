package com.TPQI.thai2learn.DTO;

public class EvidenceFileDTO {
    private Long id;
    private String originalFilename;
    private String filePath;
    private String evidenceType;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getEvidenceType() {
        return evidenceType;
    }
    public void setEvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }

}