package com.TPQI.thai2learn.DTO;

public class JwtResponseDTO {
    private String token;
    private String username;
    private String role;
    private UserContextDTO userContext;

    public JwtResponseDTO(String token, String username, String role, UserContextDTO userContext) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.userContext = userContext;
    }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public UserContextDTO getUserContext() { return userContext; }
    public void setUserContext(UserContextDTO userContext) { this.userContext = userContext; }
}