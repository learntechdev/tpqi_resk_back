package com.TPQI.thai2learn.DTO;

public class UserContextDTO {
    private String fullName;
    private IdentifierDTO identifier;

    public UserContextDTO(String fullName, IdentifierDTO identifier) {
        this.fullName = fullName;
        this.identifier = identifier;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public IdentifierDTO getIdentifier() { return identifier; }
    public void setIdentifier(IdentifierDTO identifier) { this.identifier = identifier; }
}