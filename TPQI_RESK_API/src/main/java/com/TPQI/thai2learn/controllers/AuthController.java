package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.CredentialRequestDTO;
import com.TPQI.thai2learn.DTO.IdentifierDTO;
import com.TPQI.thai2learn.DTO.JwtResponseDTO;
import com.TPQI.thai2learn.DTO.LoginRequestDTO;
import com.TPQI.thai2learn.DTO.UserContextDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.ReskUser;
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskUserRepository;
import com.TPQI.thai2learn.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ReskUserRepository reskUserRepository;

    @PostMapping("/request-credentials")
    public ResponseEntity<?> requestCredentials(@RequestBody CredentialRequestDTO requestDTO) {
        try {
            authService.processCredentialRequest(requestDTO);
            return ResponseEntity.ok(Map.of("message", "ระบบได้ส่งข้อมูลสำหรับเข้าสู่ระบบไปยังอีเมลของท่านแล้ว"));
        } catch (Exception e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : "เกิดข้อผิดพลาดที่ไม่คาดคิดในระบบ";
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
        }
    }

     @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            String jwt = authService.login(loginRequest);
            ReskUser user = reskUserRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserContextDTO userContext = buildUserContext(user);
            String role = user.getRole();

            JwtResponseDTO response = new JwtResponseDTO(jwt, user.getUsername(), role, userContext);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Username หรือ Password ไม่ถูกต้อง";
            e.printStackTrace();
            return ResponseEntity.status(401).body(Map.of("error", errorMessage));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "ผู้ใช้งานไม่ได้รับการยืนยันตัวตน"));
        }

        ReskUser user = reskUserRepository.findByUsername(authentication.getName())
                 .orElseThrow(() -> new RuntimeException("User not found"));

        UserContextDTO userContext = buildUserContext(user);
        
        Map<String, Object> userProfile = new java.util.HashMap<>();
        userProfile.put("username", user.getUsername());
        userProfile.put("role", user.getRole());
        userProfile.put("userContext", userContext);

        return ResponseEntity.ok(userProfile);
    }

    private UserContextDTO buildUserContext(ReskUser user) {
        IdentifierDTO identifier = null;
        String role = user.getRole();

        if ("ASSESSEE".equalsIgnoreCase(role)) {
            identifier = new IdentifierDTO("applicant_id", user.getAssessmentApplicantId());
        } else if ("EXAMINER".equalsIgnoreCase(role)) {
            identifier = new IdentifierDTO("examiner_code", user.getExaminerCode());
        }

        return new UserContextDTO(user.getFullName(), identifier);
    }

    @PostMapping("/admin/create-user/{appId}")
    public ResponseEntity<?> createUserByAdmin(@PathVariable String appId) {
        try {
            String generatedPassword = authService.createUserByAppId(appId);
            return ResponseEntity.ok(Map.of(
                    "message", "สร้างผู้ใช้สำเร็จ",
                    "username", appId,
                    "password", generatedPassword
            ));
        } catch (Exception e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : "เกิดข้อผิดพลาดที่ไม่คาดคิด";
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
        }
    }

    
}