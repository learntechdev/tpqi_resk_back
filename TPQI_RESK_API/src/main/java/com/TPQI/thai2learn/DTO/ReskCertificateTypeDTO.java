package com.TPQI.thai2learn.DTO;

public class ReskCertificateTypeDTO {
    private Integer id;
    private String nameTh;
    
    public ReskCertificateTypeDTO(Integer id, String nameTh) {
        this.id = id;
        this.nameTh = nameTh;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getNameTh() {
        return nameTh;
    }
}
