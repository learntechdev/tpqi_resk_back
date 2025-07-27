package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.CredentialRequestDTO;
import com.TPQI.thai2learn.DTO.LoginRequestDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentApplicant;
import com.TPQI.thai2learn.entities.tpqi_asm.ExamSchedule;
import com.TPQI.thai2learn.entities.tpqi_asm.ReskUser;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentApplicantRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.ExamScheduleRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskUserRepository;
import com.TPQI.thai2learn.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private AssessmentApplicantRepository assessmentApplicantRepository;

    @Autowired
    private ReskUserRepository reskUserRepository;

    @Autowired
    private ExamScheduleRepository examScheduleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    public String login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }

    @Transactional
    public String createUserByAppId(String appId) {
        AssessmentApplicant applicant = assessmentApplicantRepository.findByAppId(appId)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลผู้สมัคร (Assessment Applicant): " + appId));

        if (reskUserRepository.existsByAppId(appId)) {
            throw new RuntimeException("ผู้ใช้งานนี้มีบัญชีในระบบแล้ว");
        }

        String username = applicant.getAppId();
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedPassword = passwordEncoder.encode(rawPassword);
        
        String fullName = applicant.getInitialName() + applicant.getName() + " " + applicant.getLastname();

        ReskUser newUser = new ReskUser();
        newUser.setAppId(username);
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setFullName(fullName);
        newUser.setEmail(null); 
        newUser.setRole("ASSESSEE"); 
        newUser.setAccountExpirationDate(LocalDateTime.now().plusDays(30)); 
        newUser.setActive(true);

        reskUserRepository.save(newUser);
        return rawPassword;
    }

    @Transactional
    public void processCredentialRequest(CredentialRequestDTO requestDTO) {
        AssessmentApplicant applicant = assessmentApplicantRepository.findByCitizenId(requestDTO.getCitizenId())
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลผู้สมัครจากเลขบัตรประชาชนที่ระบุ"));

        String appId = applicant.getAppId();
        String examScheduleId = applicant.getExamScheduleId();
        
        if (examScheduleId == null || examScheduleId.trim().isEmpty()) {
            throw new RuntimeException("ผู้สมัครยังไม่ได้ถูกจัดสรรรอบสอบ กรุณาติดต่อเจ้าหน้าที่");
        }

        if (reskUserRepository.existsByAppId(appId)) {
            throw new RuntimeException("ท่านมีบัญชีผู้ใช้งานในระบบแล้ว กรุณาตรวจสอบอีเมลของท่าน หรือติดต่อเจ้าหน้าที่");
        }
        
        ExamSchedule schedule = examScheduleRepository.findByTpqiExamNo(examScheduleId);
        if (schedule == null) {
            throw new RuntimeException("ไม่พบข้อมูลรอบสอบ: " + examScheduleId);
        }

        LocalDateTime expirationDate;
        if (schedule.getEndDate() != null) {
            Date utilDate = new Date(schedule.getEndDate().getTime());
            expirationDate = utilDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .withHour(23).withMinute(59).withSecond(59);
        } else {
            expirationDate = LocalDateTime.now().plusDays(30);
        }

        String username = appId;
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedPassword = passwordEncoder.encode(rawPassword);

        ReskUser newUser = new ReskUser();
        newUser.setAppId(appId);
        newUser.setAssessmentApplicantId(applicant.getId());
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setFullName(applicant.getInitialName() + applicant.getName() + " " + applicant.getLastname());
        newUser.setEmail(requestDTO.getEmail());
        newUser.setRole("ASSESSEE"); 
        newUser.setAccountExpirationDate(expirationDate); 
        newUser.setActive(true);

        reskUserRepository.save(newUser);
        emailService.sendCredentialsEmail(requestDTO.getEmail(), username, rawPassword);
    }
}