package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.UserDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.ReskUser;
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskUserRepository;
import com.TPQI.thai2learn.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.TPQI.thai2learn.DTO.AdminUserDTO;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private ReskUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> getAllStaffUsers() {
        List<ReskUser> users = userRepository.findAll();
        return users.stream()
                .filter(user -> user.getRole() != Role.ROLE_ASSESSEE)
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }


    private UserDTO convertToUserDTO(ReskUser user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }

    @Transactional
    public UserDTO createStaffUser(AdminUserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
            throw new RuntimeException("Error: Password is required for new user!");
        }

        ReskUser newUser = new ReskUser();
        newUser.setUsername(userDTO.getUsername());
        newUser.setFullName(userDTO.getFullName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRole(userDTO.getRole());
        newUser.setActive(userDTO.getIsActive());
        newUser.setExaminerCode(userDTO.getExaminerCode());
        newUser.setOrgCode(userDTO.getOrgCode());

        ReskUser savedUser = userRepository.save(newUser);
        return convertToUserDTO(savedUser);
    }

    @Transactional
    public UserDTO updateStaffUser(Long userId, AdminUserDTO userDTO) {
        ReskUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setActive(userDTO.getIsActive());
        user.setExaminerCode(userDTO.getExaminerCode());
        user.setOrgCode(userDTO.getOrgCode());
        
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        ReskUser updatedUser = userRepository.save(user);
        return convertToUserDTO(updatedUser);
    }
}