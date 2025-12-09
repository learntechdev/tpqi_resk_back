package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.TPQI.thai2learn.DTO.ProxyAssessmentDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.*;
import com.TPQI.thai2learn.entities.tpqi_asm.types.AssessmentStatus;
import com.TPQI.thai2learn.repositories.tpqi_asm.*;
import com.TPQI.thai2learn.security.Role;
import org.springframework.web.multipart.MultipartFile;
import com.TPQI.thai2learn.DTO.ExaminerFilterOptionsDTO;
import com.TPQI.thai2learn.DTO.ExaminerInfoDTO;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class ExaminerProxyService {

    private final ExaminerRepository examinerRepository;
    private final ReskUserRepository reskUserRepository;
    private final AssessmentApplicantRepository assessmentApplicantRepository;
    private final AssessmentRepository assessmentRepository;
    private final ExaminerProxyLogRepository examinerProxyLogRepository;
    private final AssessmentSubmissionDetailsRepository submissionDetailsRepository;
    private final CBRepository cbRepository;
    private final AppointExaminerRepository appointExaminerRepository;
    private final ProxyPassedCompetencyRepository passedCompetencyRepository;
    private final ProxyDelegationDocumentRepository delegationDocumentRepository;

    public ExaminerProxyService(ExaminerRepository examinerRepository, ReskUserRepository reskUserRepository, AssessmentApplicantRepository assessmentApplicantRepository, AssessmentRepository assessmentRepository, ExaminerProxyLogRepository examinerProxyLogRepository, AssessmentSubmissionDetailsRepository submissionDetailsRepository, CBRepository cbRepository, AppointExaminerRepository appointExaminerRepository, ProxyPassedCompetencyRepository passedCompetencyRepository, ProxyDelegationDocumentRepository delegationDocumentRepository) {
        this.examinerRepository = examinerRepository;
        this.reskUserRepository = reskUserRepository;
        this.assessmentApplicantRepository = assessmentApplicantRepository;
        this.assessmentRepository = assessmentRepository;
        this.examinerProxyLogRepository = examinerProxyLogRepository;
        this.submissionDetailsRepository = submissionDetailsRepository;
        this.cbRepository = cbRepository;
        this.appointExaminerRepository = appointExaminerRepository;
        this.passedCompetencyRepository = passedCompetencyRepository;
        this.delegationDocumentRepository = delegationDocumentRepository;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public Page<AssessmentInfoDTO> getAllExamRounds(Authentication authentication, String search, String qualification, String level, String tool, Pageable pageable) {
        ReskUser user = getUserFromAuthentication(authentication);
        validateExaminerProxy(user);
        return examinerRepository.findAllExamRounds(search, qualification, level, tool, pageable);
    }

    private ReskUser getUserFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return reskUserRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("User not found: " + username));
    }

    private void validateExaminerProxy(ReskUser user) {
        if (user.getRole() != Role.ROLE_EXAMINER_PROXY) {
            throw new AccessDeniedException("User does not have EXAMINER_PROXY role.");
        }
    }

    private String storeDelegationFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty delegation file.");
        }
        try {
            Path delegationUploadPath = Paths.get(uploadDir, "delegations");
            Files.createDirectories(delegationUploadPath);

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + fileExtension;
            
            Path destinationFile = delegationUploadPath.resolve(newFilename).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile);

            return "/uploads/delegations/" + newFilename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store delegation file.", e);
        }
    }

    @Transactional
    public void submitProxyAssessment(Authentication authentication, ProxyAssessmentDTO assessmentDTO, List<MultipartFile> delegationFiles) {
        ReskUser proxyUser = getUserFromAuthentication(authentication);
        validateExaminerProxy(proxyUser);

        AssessmentApplicant applicant = assessmentApplicantRepository.findById(assessmentDTO.getApplicantId())
                .orElseThrow(() -> new RuntimeException("Applicant not found: " + assessmentDTO.getApplicantId()));

        boolean isExaminerAppointed = appointExaminerRepository.existsByTpqiExamNoAndExaminerCode(
            applicant.getExamScheduleId(), 
            assessmentDTO.getOriginalExaminerCode()
        );

        if (!isExaminerAppointed) {
            throw new RuntimeException("Original examiner is not appointed for this exam round: " + assessmentDTO.getOriginalExaminerCode());
        }
        
        List<String> savedFilePaths = new ArrayList<>();
        if (delegationFiles != null && !delegationFiles.isEmpty()) {
            for (MultipartFile file : delegationFiles) {
                savedFilePaths.add(storeDelegationFile(file));
            }
        } else {
            throw new RuntimeException("Delegation file is required.");
        }
        
        Assessment assessment = saveOrUpdateAssessmentRecord(applicant, assessmentDTO);

        ExaminerProxyLog proxyLog = new ExaminerProxyLog();
        proxyLog.setAssessmentId(assessment.getAssessmentId());
        proxyLog.setApplicantId(applicant.getId());
        proxyLog.setOriginalExaminerCode(assessmentDTO.getOriginalExaminerCode());
        proxyLog.setProxyExaminerCode(proxyUser.getUsername());
        ExaminerProxyLog savedProxyLog = examinerProxyLogRepository.save(proxyLog);

        List<ProxyDelegationDocument> docsToSave = new ArrayList<>();
        for (String path : savedFilePaths) {
            docsToSave.add(new ProxyDelegationDocument(savedProxyLog.getId(), path));
        }
        delegationDocumentRepository.saveAll(docsToSave);

        if ("PASS".equalsIgnoreCase(assessmentDTO.getResultStatus()) && assessmentDTO.getPassedCompetencyCodes() != null && !assessmentDTO.getPassedCompetencyCodes().isEmpty()) {
            
            List<ProxyPassedCompetency> passedItems = new ArrayList<>();
            for (String code : assessmentDTO.getPassedCompetencyCodes()) {
                passedItems.add(new ProxyPassedCompetency(savedProxyLog.getId(), code));
            }
            passedCompetencyRepository.saveAll(passedItems);
        }

        AssessmentStatus newStatus = "PASS".equalsIgnoreCase(assessmentDTO.getResultStatus()) ? AssessmentStatus.EVALUATED_PASS : AssessmentStatus.EVALUATED_FAIL;
        applicant.setAssessmentStatus(newStatus);
        assessmentApplicantRepository.save(applicant);

        updateSubmissionDetailsStatus(applicant.getId(), assessment.getExamResult());
    }

    private Assessment saveOrUpdateAssessmentRecord(AssessmentApplicant applicant, ProxyAssessmentDTO assessmentDTO) {
        
        Assessment assessment = assessmentRepository.findByAppId(applicant.getAppId())
                .orElseGet(() -> {
                    Assessment newAssessment = new Assessment();
                    newAssessment.setAppId(applicant.getAppId());
                    newAssessment.setExamScheduleId(applicant.getExamScheduleId());
                    newAssessment.setCitizenId(applicant.getCitizenId());
                    newAssessment.setToolType(applicant.getAsmToolType());
                    newAssessment.setType("2");
                    newAssessment.setTotalScore(0);
                    newAssessment.setFullScore(0);
                    newAssessment.setExamPercentScore(BigDecimal.ZERO);
                    return newAssessment;
                });
        
        String resultText = "PASS".equalsIgnoreCase(assessmentDTO.getResultStatus()) ? "ผ่าน" : "ไม่ผ่าน";

        assessment.setAssessmentDate(new Date());
        assessment.setExamResult(resultText);
        assessment.setRecomment(assessmentDTO.getComments());

        return assessmentRepository.save(assessment);
    }
    
    private void updateSubmissionDetailsStatus(Long applicantId, String resultText) {
        AssessmentSubmissionDetails submissionDetails = submissionDetailsRepository.findByAssessmentApplicantId(applicantId)
                .orElse(new AssessmentSubmissionDetails());
        submissionDetails.setAssessmentApplicantId(applicantId);
        submissionDetails.setSubmissionStatus(resultText);
        submissionDetailsRepository.save(submissionDetails);
    }

    @Transactional(readOnly = true)
    public ExaminerFilterOptionsDTO getFilterOptions(Authentication authentication) {
        validateExaminerProxy(getUserFromAuthentication(authentication));

        ExaminerFilterOptionsDTO options = new ExaminerFilterOptionsDTO();
        
        List<String> allOccLevelNames = examinerRepository.findAllDistinctOccLevelNames();
        List<String> allTools = examinerRepository.findAllDistinctAssessmentTools();

        Set<String> qualifications = new HashSet<>();
        Set<String> levels = new HashSet<>();

        for (String occLevelName : allOccLevelNames) {
            if (occLevelName != null && !occLevelName.isEmpty()) {
                String[] parts = occLevelName.split("ระดับ", 2);
                qualifications.add(parts[0].trim());
                if (parts.length > 1) {
                    levels.add(parts[1].trim());
                }
            }
        }

        List<String> sortedQualifications = new ArrayList<>(qualifications);
        Collections.sort(sortedQualifications);
        options.setQualifications(sortedQualifications);

        List<String> sortedLevels = new ArrayList<>(levels);
        Collections.sort(sortedLevels);
        options.setLevels(sortedLevels);
        
        options.setAssessmentTools(allTools);

        return options;
    }

    @Transactional(readOnly = true)
    public Page<CbApplicantSummaryDTO> getApplicantsByExamRound(
            Authentication authentication, String tpqiExamNo, String search, String status, Pageable pageable) {
        
        validateExaminerProxy(getUserFromAuthentication(authentication));
        return cbRepository.findApplicantSummariesByExamRound(tpqiExamNo, search, status, pageable);
    }

    @Transactional(readOnly = true)
    public List<ExaminerInfoDTO> getExaminersByExamRound(String tpqiExamNo) {
        return appointExaminerRepository.findByTpqiExamNo(tpqiExamNo)
                .stream()
                .map(appointment -> new ExaminerInfoDTO(
                    appointment.getExaminerCode(), 
                    appointment.getExaminerFullName()
                ))
                .collect(Collectors.toList());
    }
}