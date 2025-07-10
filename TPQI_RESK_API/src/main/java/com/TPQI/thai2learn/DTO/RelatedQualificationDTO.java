package com.TPQI.thai2learn.DTO;

public class RelatedQualificationDTO {
    private Long id; 
    private String qualificationName; 

    public RelatedQualificationDTO(Long id, String qualificationName) {
        this.id = id;
        this.qualificationName = qualificationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }
}