package com.TPQI.thai2learn.DTO;

public class StatusDTO {
    private Integer key;
    private String displayName;

    public StatusDTO(Integer key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public Integer getKey() { return key; }
    public void setKey(Integer key) { this.key = key; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}