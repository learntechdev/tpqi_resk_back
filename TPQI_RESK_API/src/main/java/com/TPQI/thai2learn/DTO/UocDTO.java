package com.TPQI.thai2learn.DTO;

import java.util.List;

public class UocDTO {
    private String uocId;
    private String uocCode;
    private String uocName;
    private List<EocDTO> eocs;

    public String getUocId() { return uocId; }
    public void setUocId(String uocId) { this.uocId = uocId; }

    public String getUocCode() { return uocCode; }
    public void setUocCode(String uocCode) { this.uocCode = uocCode; }

    public String getUocName() { return uocName; }
    public void setUocName(String uocName) { this.uocName = uocName; }

    public List<EocDTO> getEocs() { return eocs; }
    public void setEocs(List<EocDTO> eocs) { this.eocs = eocs; }
}