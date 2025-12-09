package com.TPQI.thai2learn.DTO;

import com.TPQI.thai2learn.security.Role;

public class UserDTO {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Role role;
    private boolean isActive;

    public UserDTO() {}

    public UserDTO(Long id, String username, String fullName, String email, Role role, boolean isActive) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}