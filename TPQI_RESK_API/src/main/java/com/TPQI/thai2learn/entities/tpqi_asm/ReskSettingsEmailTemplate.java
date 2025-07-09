package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resk_settings_email_template")
public class ReskSettingsEmailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "et_id")
    private Integer etId;

    @Column(name = "et_name")
    private String etName;
    
    @Column(name = "et_description")
    private String etDescription;

    @Column(name = "et_subject")
    private String etSubject;

    @Column(name = "et_email_content", columnDefinition = "TEXT")
    private String etEmailContent;

    @Column(name = "et_status", nullable = false, columnDefinition = "int default 1")
    private Integer etStatus = 1;

    @Column(name = "create_date")
    private LocalDateTime createdAt;

    @Column(name = "update_date")
    private LocalDateTime updatedAt;

    // --- Getters and Setters ---

    public Integer getEtId() {
        return etId;
    }

    public void setEtId(Integer etId) {
        this.etId = etId;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
