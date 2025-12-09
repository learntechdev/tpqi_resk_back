package com.TPQI.thai2learn.DTO;

import com.TPQI.thai2learn.security.Role;
import jakarta.validation.constraints.NotNull;

public class UserRoleUpdateDTO {

    @NotNull(message = "Role is required")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}