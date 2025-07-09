package com.TPQI.thai2learn.DTO;

public class EmailTemplateRequestDTO {

    private String etName;
    private String etDescription;
    private String etSubject;
    private String etEmailContent;
    private Integer etStatus;

    // --- Getters and Setters ---

    public String getEtName() {
        return etName;
    }

    public void setEtName(String etName) {
        this.etName = etName;
    }

    public String getEtDescription() {
        return etDescription;
    }

    public void setEtDescription(String etDescription) {
        this.etDescription = etDescription;
    }

    public String getEtSubject() {
        return etSubject;
    }

    public void setEtSubject(String etSubject) {
        this.etSubject = etSubject;
    }

    public String getEtEmailContent() {
        return etEmailContent;
    }

    public void setEtEmailContent(String etEmailContent) {
        this.etEmailContent = etEmailContent;
    }

    public Integer getEtStatus() {
        return etStatus;
    }

    public void setEtStatus(Integer etStatus) {
        this.etStatus = etStatus;
    }
}
