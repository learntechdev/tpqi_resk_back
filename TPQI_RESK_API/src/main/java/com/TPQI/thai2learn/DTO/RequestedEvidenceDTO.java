package com.TPQI.thai2learn.DTO;

import jakarta.validation.constraints.NotBlank;

public class RequestedEvidenceDTO {

    @NotBlank(message = "UOC Code is required")
    private String uocCode;

    private String details;

    public String getUocCode() { return uocCode; }
    public void setUocCode(String uocCode) { this.uocCode = uocCode; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}