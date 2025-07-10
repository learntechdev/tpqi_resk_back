package com.TPQI.thai2learn.DTO;

public class PcDTO {
    private String pcId;
    private String pcName;

    private transient String parentEocId;

    public PcDTO(String pcId, String pcName) {
        this.pcId = pcId;
        this.pcName = pcName;
    }

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public String getParentEocId() {
        return parentEocId;
    }

    public void setParentEocId(String parentEocId) {
        this.parentEocId = parentEocId;
    }
}