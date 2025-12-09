package com.TPQI.thai2learn.DTO;

public class UnlinkedPcDTO {
    private String pcId;
    private String pcName;
    private String parentEocId;

    public UnlinkedPcDTO() {}

    public String getPcId() { return pcId; }
    public void setPcId(String pcId) { this.pcId = pcId; }

    public String getPcName() { return pcName; }
    public void setPcName(String pcName) { this.pcName = pcName; }

    public String getParentEocId() { return parentEocId; }
    public void setParentEocId(String parentEocId) { this.parentEocId = parentEocId; }
}
