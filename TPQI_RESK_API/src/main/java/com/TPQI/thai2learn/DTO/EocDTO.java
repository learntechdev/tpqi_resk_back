package com.TPQI.thai2learn.DTO;

import java.util.List;

public class EocDTO {
    private String eocId;
    private String eocCode;
    private String eocName;
    private List<PcDTO> performanceCriteria; 

    private transient String parentUocId;

    public EocDTO(String eocId, String eocCode, String eocName, List<PcDTO> performanceCriteria) {
        this.eocId = eocId;
        this.eocCode = eocCode;
        this.eocName = eocName;
        this.performanceCriteria = performanceCriteria;
    }

    public String getEocId() {
        return eocId;
    }

    public void setEocId(String eocId) {
        this.eocId = eocId;
    }

    public String getEocCode() {
        return eocCode;
    }

    public void setEocCode(String eocCode) {
        this.eocCode = eocCode;
    }

    public String getEocName() {
        return eocName;
    }

    public void setEocName(String eocName) {
        this.eocName = eocName;
    }

    public List<PcDTO> getPerformanceCriteria() {
        return performanceCriteria;
    }

    public void setPerformanceCriteria(List<PcDTO> performanceCriteria) {
        this.performanceCriteria = performanceCriteria;
    }

    public String getParentUocId() {
        return parentUocId;
    }

    public void setParentUocId(String parentUocId) {
        this.parentUocId = parentUocId;
    }
}