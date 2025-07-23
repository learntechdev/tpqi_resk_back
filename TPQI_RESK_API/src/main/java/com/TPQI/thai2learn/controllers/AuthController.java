package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.CredentialRequestDTO;
import com.TPQI.thai2learn.DTO.JwtResponseDTO;
import com.TPQI.thai2learn.DTO.LoginRequestDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.ReskUser;
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskUserRepository;
import com.TPQI.thai2learn.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst().orElse(null);

            if (role != null && role.startsWith("ROLE_")) {
                role = role.substring(5);
            }

            ReskUser user = reskUserRepository.findByUsername(loginRequest.getUsername()).orElse(null);
            Long applicantId = (user != null) ? user.getAssessmentApplicantId() : null;

            return ResponseEntity.ok(new JwtResponseDTO(jwt, loginRequest.getUsername(), role, applicantId));
        } catch (Exception e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Username หรือ Password ไม่ถูกต้อง";
            e.printStackTrace();
            return ResponseEntity.status(401).body(Map.of("error", errorMessage));
        }
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