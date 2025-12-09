package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.UserDTO;
import com.TPQI.thai2learn.security.Role;
import com.TPQI.thai2learn.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.TPQI.thai2learn.DTO.AdminUserDTO;

import java.util.Arrays;
import java.util.List;
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
    public ResponseEntity<UserDTO> createStaffUser(@Valid @RequestBody AdminUserDTO userDTO) {
        UserDTO createdUser = adminService.createStaffUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateStaffUser(@PathVariable Long userId, @Valid @RequestBody AdminUserDTO userDTO) {
        UserDTO updatedUser = adminService.updateStaffUser(userId, userDTO);
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