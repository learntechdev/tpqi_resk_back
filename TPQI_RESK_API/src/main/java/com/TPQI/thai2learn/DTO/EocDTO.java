package com.TPQI.thai2learn.DTO;

import java.util.List;

public class EocDTO {
    private String eocId;
    private String eocCode;
    private String eocName;
    private String uocId;
    private List<PcDTO> pcs;

    public String getEocId() { return eocId; }
    public void setEocId(String eocId) { this.eocId = eocId; }

    public String getEocCode() { return eocCode; }
    public void setEocCode(String eocCode) { this.eocCode = eocCode; }

    public String getEocName() { return eocName; }
    public void setEocName(String eocName) { this.eocName = eocName; }

    public String getUocId() { return uocId; }
    public void setUocId(String uocId) { this.uocId = uocId; }

    public List<PcDTO> getPcs() { return pcs; }
    public void setPcs(List<PcDTO> pcs) { this.pcs = pcs; }
}