package com.TPQI.thai2learn.entities.tpqi_asm;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.TPQI.thai2learn.security.Role;
@Entity
@Table(name = "resk_users") 
public class ReskUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_id", unique = true)
    private String appId; 

    @Column(name = "assessment_applicant_id")
    private Long assessmentApplicantId;

    @Column(nullable = false, unique = true)
    private String username; 

    @Column(nullable = false)
    private String password; 
    
    @Column(name = "full_name")
    private String fullName;

    @Column
    private String email;
    
    @Column(name = "examiner_code")
    private String examinerCode;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "account_expiration_date") 
    private LocalDateTime accountExpirationDate; 

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; 

    @Column(name = "org_code")
    private String orgCode;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }

    public Long getAssessmentApplicantId() { return assessmentApplicantId; }
    public void setAssessmentApplicantId(Long assessmentApplicantId) { this.assessmentApplicantId = assessmentApplicantId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getAccountExpirationDate() { return accountExpirationDate; }
    public void setAccountExpirationDate(LocalDateTime accountExpirationDate) { this.accountExpirationDate = accountExpirationDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }

    public String getExaminerCode() { return examinerCode; }
    public void setExaminerCode(String examinerCode) { this.examinerCode = examinerCode; }
    
    public String getOrgCode() { return orgCode; }
    public void setOrgCode(String orgCode) { this.orgCode = orgCode; }
}