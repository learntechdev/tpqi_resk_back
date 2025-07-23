package com.TPQI.thai2learn.DTO;

import java.util.List;

public class UnlinkedUocDTO {
    private String uocId;
    private String uocCode;
    private String uocName;
    private List<UnlinkedEocDTO> elementsOfCompetency;

    public UnlinkedUocDTO() {}

    public String getUocId() { return uocId; }
    public void setUocId(String uocId) { this.uocId = uocId; }

    public String getUocCode() { return uocCode; }
    public void setUocCode(String uocCode) { this.uocCode = uocCode; }

    public String getUocName() { return uocName; }
    public void setUocName(String uocName) { this.uocName = uocName; }

    public List<UnlinkedEocDTO> getElementsOfCompetency() { return elementsOfCompetency; }
    public void setElementsOfCompetency(List<UnlinkedEocDTO> elementsOfCompetency) { this.elementsOfCompetency = elementsOfCompetency; }
}
