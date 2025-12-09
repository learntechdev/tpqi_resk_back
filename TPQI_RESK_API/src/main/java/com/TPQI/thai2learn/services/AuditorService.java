package com.TPQI.thai2learn.services;
import com.TPQI.thai2learn.DTO.AssessmentCancellationDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentApplicant;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentCancellationLog;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentSubmissionDetails;
import com.TPQI.thai2learn.entities.tpqi_asm.types.AssessmentStatus;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentApplicantRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentCancellationLogRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentSubmissionDetailsRepository;
import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.ExaminerFilterOptionsDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.ReskUser;
import com.TPQI.thai2learn.repositories.tpqi_asm.CBRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.ExaminerRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskUserRepository;
import com.TPQI.thai2learn.security.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuditorService {

    private final ReskUserRepository reskUserRepository;
    private final ExaminerRepository examinerRepository;
    private final CBRepository cbRepository;
    private final AssessmentApplicantRepository applicantRepository;
    private final AssessmentCancellationLogRepository cancellationLogRepository;
    private final AssessmentSubmissionDetailsRepository submissionDetailsRepository;

    public AuditorService(ReskUserRepository reskUserRepository, ExaminerRepository examinerRepository, CBRepository cbRepository, AssessmentApplicantRepository applicantRepository, AssessmentCancellationLogRepository cancellationLogRepository, AssessmentSubmissionDetailsRepository submissionDetailsRepository) {
        this.reskUserRepository = reskUserRepository;
        this.examinerRepository = examinerRepository;
        this.cbRepository = cbRepository;
        this.applicantRepository = applicantRepository;
        this.cancellationLogRepository = cancellationLogRepository;
        this.submissionDetailsRepository = submissionDetailsRepository;
    }

    private ReskUser getUserFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return reskUserRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("User not found: " + username));
    }

    private void validateAuditor(ReskUser user) {
        if (user.getRole() != Role.ROLE_AUDITOR) {
            throw new AccessDeniedException("User does not have AUDITOR role.");
        }
    }

    @Transactional(readOnly = true)
    public Page<AssessmentInfoDTO> getAllExamRounds(Authentication authentication, String search, String qualification, String level, String tool, Pageable pageable) {
        validateAuditor(getUserFromAuthentication(authentication));
        return examinerRepository.findAllExamRounds(search, qualification, level, tool, pageable);
    }

    @Transactional(readOnly = true)
    public ExaminerFilterOptionsDTO getFilterOptions(Authentication authentication) {
        validateAuditor(getUserFromAuthentication(authentication));
        List<String> allOccLevelNames = examinerRepository.findAllDistinctOccLevelNames();
        List<String> allTools = examinerRepository.findAllDistinctAssessmentTools();
        
        ExaminerFilterOptionsDTO options = new ExaminerFilterOptionsDTO();
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
    public Page<CbApplicantSummaryDTO> getApplicantsByExamRound(Authentication authentication, String tpqiExamNo, String search, String status, Pageable pageable) {
        validateAuditor(getUserFromAuthentication(authentication));
        return cbRepository.findApplicantSummariesByExamRound(tpqiExamNo, search, status, pageable);
    }

    @Transactional
    public void cancelAssessment(Authentication authentication, AssessmentCancellationDTO cancellationDTO) {
        ReskUser auditor = getUserFromAuthentication(authentication);
        validateAuditor(auditor);

        AssessmentApplicant applicant = applicantRepository.findById(cancellationDTO.getApplicantId())
            .orElseThrow(() -> new RuntimeException("Applicant not found with id: " + cancellationDTO.getApplicantId()));

        applicant.setAssessmentStatus(AssessmentStatus.CANCELLED);
        applicantRepository.save(applicant);

        AssessmentSubmissionDetails submissionDetails = submissionDetailsRepository.findByAssessmentApplicantId(applicant.getId())
                .orElse(new AssessmentSubmissionDetails());
        submissionDetails.setAssessmentApplicantId(applicant.getId());
        submissionDetails.setSubmissionStatus(AssessmentStatus.CANCELLED.getDisplayName());
        submissionDetailsRepository.save(submissionDetails);

        AssessmentCancellationLog log = new AssessmentCancellationLog();
        log.setApplicantId(applicant.getId());
        log.setAuditorUsername(auditor.getUsername());
        log.setReason(cancellationDTO.getReason());
        cancellationLogRepository.save(log);
    }
}