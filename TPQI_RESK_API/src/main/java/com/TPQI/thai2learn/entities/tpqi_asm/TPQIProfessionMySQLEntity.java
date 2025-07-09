package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;

@Entity
@Table(name = "resk_tpqi_profession")
public class TPQIProfessionMySQLEntity {

    @Id
    @Column(name = "profession_id")  // ชื่อคอลัมน์ในฐานข้อมูลที่ตรงกับฟิลด์นี้
    private int professionId;

    @Column(name = "profession_code")
    private String professionCode;

    @Column(name = "profession_name")
    private String professionName;

    @Column(name = "status")
    private String status;

    // --- Constructors ---
    public TPQIProfessionMySQLEntity() {
    }

    public TPQIProfessionMySQLEntity(int professionId, String professionCode, String professionName, String status) {
        this.professionId = professionId;
        this.professionCode = professionCode;
        this.professionName = professionName;
        this.status = status;
    }

    // --- Getters and Setters ---

    public int getProfessionId() {
        return professionId;
    }

    public void setProfessionId(int professionId) {
        this.professionId = professionId;
    }

    public String getProfessionCode() {
        return professionCode;
    }

    public void setProfessionCode(String professionCode) {
        this.professionCode = professionCode;
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // --- toString() ---

    @Override
    public String toString() {
        return "TPQIProfessionMySQLEntity{" +
                "professionId=" + professionId +
                ", professionCode='" + professionCode + '\'' +
                ", professionName='" + professionName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
