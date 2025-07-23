package com.TPQI.thai2learn.DTO;
import java.util.List;

public class EvidenceDetailDTO {
    private Long fileId;
    private List<String> evidenceTypes;

    public Long getFileId() { return fileId; }
    public void setFileId(Long fileId) { this.fileId = fileId; }
    public List<String> getEvidenceTypes() { return evidenceTypes; }
    public void setEvidenceTypes(List<String> evidenceTypes) { this.evidenceTypes = evidenceTypes; }
}