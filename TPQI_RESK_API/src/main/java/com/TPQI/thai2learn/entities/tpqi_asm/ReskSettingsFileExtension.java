package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;

@Entity
@Table(name = "resk_settings_file_extension", schema = "tpqinet_asm_uat")
public class ReskSettingsFileExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "file_ext_id")  
    private Integer fileExtId;

    @Column(name = "file_ext", nullable = false, length = 10)
    private String fileExtName;

    @Column(name = "file_max_size", nullable = false)
    private Integer fileMaxSize;
/*
    @Column(name = "create_by", length = 50)
    private String createBy;

    @Column(name = "createdate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
*/
    // getters and setters (หรือใช้ Lombok @Data/@Getter/@Setter)

    public Integer getFileExtId() {
        return fileExtId;
    }

    public void setFileExtId(Integer fileExtId) {
        this.fileExtId = fileExtId;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public Integer getFileMaxSize() {
        return fileMaxSize;
    }

    public void setFileMaxSize(Integer fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }
/*
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }*/
}
