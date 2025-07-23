package com.TPQI.thai2learn.DTO;

public class JwtResponseDTO {
    private String token;
    private String username;
    private String role;
    private Long applicantId;

    public JwtResponseDTO(String token, String username, String role, Long applicantId) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.applicantId = applicantId;
    }


    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
}