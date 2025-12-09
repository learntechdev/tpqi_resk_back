package com.TPQI.thai2learn.DTO;

import java.util.List;

public class UnlinkedEocDTO {
    private String eocId;
    private String eocCode;
    private String eocName;
    private String parentUocId;
    private List<UnlinkedPcDTO> performanceCriteria;

    public UnlinkedEocDTO() {}

    public String getEocId() { return eocId; }
    public void setEocId(String eocId) { this.eocId = eocId; }

    public String getEocCode() { return eocCode; }
    public void setEocCode(String eocCode) { this.eocCode = eocCode; }

    public String getEocName() { return eocName; }
    public void setEocName(String eocName) { this.eocName = eocName; }

    public String getParentUocId() { return parentUocId; }
    public void setParentUocId(String parentUocId) { this.parentUocId = parentUocId; }

    public List<UnlinkedPcDTO> getPerformanceCriteria() { return performanceCriteria; }
    public void setPerformanceCriteria(List<UnlinkedPcDTO> performanceCriteria) { this.performanceCriteria = performanceCriteria; }
}
