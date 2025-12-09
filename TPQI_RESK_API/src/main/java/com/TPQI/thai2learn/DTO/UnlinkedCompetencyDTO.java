package com.TPQI.thai2learn.DTO;

public class UnlinkedCompetencyDTO {
    private String uocCode;
    private String uocName;

    public UnlinkedCompetencyDTO(String uocCode, String uocName) {
        this.uocCode = uocCode;
        this.uocName = uocName;
    }

    public String getUocCode() { return uocCode; }
    public void setUocCode(String uocCode) { this.uocCode = uocCode; }
    public String getUocName() { return uocName; }
    public void setUocName(String uocName) { this.uocName = uocName; }
}