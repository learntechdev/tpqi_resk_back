package com.TPQI.thai2learn.DTO;

public class RequestedEvidenceInfoDTO {

    private String uocCode;
    private String uocName;
    private String details;

    public RequestedEvidenceInfoDTO(String uocCode, String uocName, String details) {
        this.uocCode = uocCode;
        this.uocName = uocName;
        this.details = details;
    }

    public String getUocCode() { return uocCode; }
    public void setUocCode(String uocCode) { this.uocCode = uocCode; }
    public String getUocName() { return uocName; }
    public void setUocName(String uocName) { this.uocName = uocName; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}