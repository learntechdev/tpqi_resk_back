package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.UserCreationDTO;
import com.TPQI.thai2learn.DTO.UserDTO;
import com.TPQI.thai2learn.DTO.UserRoleUpdateDTO;
import com.TPQI.thai2learn.security.Role;
import com.TPQI.thai2learn.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllStaffUsers() {
        List<UserDTO> users = adminService.getAllStaffUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createStaffUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        UserDTO createdUser = adminService.createStaffUser(userCreationDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserRole(@PathVariable Long userId, @Valid @RequestBody UserRoleUpdateDTO roleUpdateDTO) {
        UserDTO updatedUser = adminService.updateUserRole(userId, roleUpdateDTO.getRole());
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserStatus(@PathVariable Long userId, @RequestBody Map<String, Boolean> statusMap) {
        Boolean isActive = statusMap.get("isActive");
        if (isActive == null) {
            return ResponseEntity.badRequest().build();
        }
        UserDTO updatedUser = adminService.updateUserStatus(userId, isActive);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = Arrays.stream(Role.values())
                .filter(role -> role != Role.ROLE_ASSESSEE)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }
}