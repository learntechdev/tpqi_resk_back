package com.TPQI.thai2learn.DTO;

import java.util.List;

public class UocDTO {
    private String uocId;
    private String uocCode;
    private String uocName;
    private List<EocDTO> elementsOfCompetency; 

    public UocDTO(String uocId, String uocCode, String uocName, List<EocDTO> elementsOfCompetency) {
        this.uocId = uocId;
        this.uocCode = uocCode;
        this.uocName = uocName;
        this.elementsOfCompetency = elementsOfCompetency;
    }

    public String getUocId() {
        return uocId;
    }

    public void setUocId(String uocId) {
        this.uocId = uocId;
    }

    public String getUocCode() {
        return uocCode;
    }

    public void setUocCode(String uocCode) {
        this.uocCode = uocCode;
    }

    public String getUocName() {
        return uocName;
    }

    public void setUocName(String uocName) {
        this.uocName = uocName;
    }

    public List<EocDTO> getElementsOfCompetency() {
        return elementsOfCompetency;
    }

    public void setElementsOfCompetency(List<EocDTO> elementsOfCompetency) {
        this.elementsOfCompetency = elementsOfCompetency;
    }
}