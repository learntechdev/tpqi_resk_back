package com.TPQI.thai2learn.DTO;

import java.util.List;

public class EvidenceLinkDTO {
    private Long fileId; 
    private List<String> competencyCodes; 

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public List<String> getCompetencyCodes() {
        return competencyCodes;
    }

    public void setCompetencyCodes(List<String> competencyCodes) {
        this.competencyCodes = competencyCodes;
    }
}