package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.ExaminerAssessmentDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.*;
import com.TPQI.thai2learn.entities.tpqi_asm.types.AssessmentStatus;
import com.TPQI.thai2learn.repositories.tpqi_asm.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExaminerService {

    private final ExaminerRepository examinerRepository;
    private final AppointExaminerRepository appointExaminerRepository;
    private final ReskUserRepository reskUserRepository;
    private final CBRepository cbRepository;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentApplicantRepository assessmentApplicantRepository;
    private final AssessmentSubmissionDetailsRepository submissionDetailsRepository;
    private final ReskRequestedEvidenceRepository requestedEvidenceRepository;

    public ExaminerService(ExaminerRepository examinerRepository,
                           AppointExaminerRepository appointExaminerRepository,
                           ReskUserRepository reskUserRepository,
                           CBRepository cbRepository,
                           AssessmentRepository assessmentRepository,
                           AssessmentApplicantRepository assessmentApplicantRepository,
                           AssessmentSubmissionDetailsRepository submissionDetailsRepository,
                           ReskRequestedEvidenceRepository requestedEvidenceRepository) {
        this.examinerRepository = examinerRepository;
        this.appointExaminerRepository = appointExaminerRepository;
        this.reskUserRepository = reskUserRepository;
        this.cbRepository = cbRepository;
        this.assessmentRepository = assessmentRepository;
        this.assessmentApplicantRepository = assessmentApplicantRepository;
        this.submissionDetailsRepository = submissionDetailsRepository;
        this.requestedEvidenceRepository = requestedEvidenceRepository;
    }


    @Transactional(readOnly = true)
    public Page<AssessmentInfoDTO> getExamRoundsForExaminer(Authentication authentication, String search, Pageable pageable) {
        ReskUser user = getUserFromAuthentication(authentication);
        validateExaminer(user);

        List<String> examCodes = appointExaminerRepository.findExamCodesByExaminerCode(user.getExaminerCode());
        if (examCodes.isEmpty()) {
            return Page.empty();
        }

        return examinerRepository.findExamRoundsByExamCodes(examCodes, search, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CbApplicantSummaryDTO> getApplicantsByExamRound(Authentication authentication, String tpqiExamNo, String search, String status, Pageable pageable) {
        ReskUser user = getUserFromAuthentication(authentication);
        validateExaminer(user);

        List<String> assignedExamCodes = appointExaminerRepository.findExamCodesByExaminerCode(user.getExaminerCode());
        if (!assignedExamCodes.contains(tpqiExamNo)) {
            throw new AccessDeniedException("You are not assigned to this exam round.");
        }

        return cbRepository.findApplicantSummariesByExamRound(tpqiExamNo, search, status, pageable);
    }

    @Transactional
    public void submitAssessment(Authentication authentication, ExaminerAssessmentDTO assessmentDTO) {
        ReskUser examiner = getUserFromAuthentication(authentication);
        validateExaminer(examiner);

        AssessmentApplicant applicant = assessmentApplicantRepository.findById(assessmentDTO.getApplicantId())
                .orElseThrow(() -> new RuntimeException("Applicant not found: " + assessmentDTO.getApplicantId()));

        List<String> assignedExamCodes = appointExaminerRepository.findExamCodesByExaminerCode(examiner.getExaminerCode());
        if (!assignedExamCodes.contains(applicant.getExamScheduleId())) {
            throw new AccessDeniedException("You are not assigned to assess this applicant.");
        }

        AssessmentStatus newStatus = mapStringToAssessmentStatus(assessmentDTO.getResultStatus());

        saveOrUpdateAssessmentRecord(applicant, assessmentDTO);

        applicant.setAssessmentStatus(newStatus);
        assessmentApplicantRepository.save(applicant);

        updateSubmissionDetailsStatus(applicant.getId(), assessmentDTO.getResultStatus());

        handleRequestedEvidences(examiner, applicant, assessmentDTO);
    }

    private AssessmentStatus mapStringToAssessmentStatus(String statusString) {
        return switch (statusString) {
            case "ผ่าน" -> AssessmentStatus.EVALUATED_PASS;
            case "ไม่ผ่าน" -> AssessmentStatus.EVALUATED_FAIL;
            case "ขอหลักฐานเพิ่มเติม" -> AssessmentStatus.MORE_EVIDENCE_REQUESTED;
            default -> throw new IllegalArgumentException("Invalid assessment status string: " + statusString);
        };
    }

    private void saveOrUpdateAssessmentRecord(AssessmentApplicant applicant, ExaminerAssessmentDTO assessmentDTO) {
        Optional<Assessment> existingAssessment = assessmentRepository.findByAppId(applicant.getAppId());
        Assessment assessment = existingAssessment.orElse(new Assessment());

        assessment.setAppId(applicant.getAppId());
        assessment.setExamScheduleId(applicant.getExamScheduleId());
        assessment.setCitizenId(applicant.getCitizenId());
        assessment.setToolType(applicant.getAsmToolType());
        assessment.setAssessmentDate(new Date());
        assessment.setExamResult(assessmentDTO.getResultStatus());
        assessment.setRecomment(assessmentDTO.getComments());
        assessment.setTotalScore(0);
        assessment.setFullScore(0);
        assessment.setExamPercentScore(BigDecimal.ZERO);
        assessment.setType("2");
        assessmentRepository.save(assessment);
    }

    private void updateSubmissionDetailsStatus(Long applicantId, String status) {
        AssessmentSubmissionDetails submissionDetails = submissionDetailsRepository.findByAssessmentApplicantId(applicantId)
                .orElse(new AssessmentSubmissionDetails());
        submissionDetails.setAssessmentApplicantId(applicantId);
        submissionDetails.setSubmissionStatus(status);
        submissionDetailsRepository.save(submissionDetails);
    }

    private void handleRequestedEvidences(ReskUser examiner, AssessmentApplicant applicant, ExaminerAssessmentDTO assessmentDTO) {
        if ("ขอหลักฐานเพิ่มเติม".equals(assessmentDTO.getResultStatus())) {
            if (assessmentDTO.getRequestedEvidences() != null && !assessmentDTO.getRequestedEvidences().isEmpty()) {

                List<ReskRequestedEvidence> evidencesToSave = assessmentDTO.getRequestedEvidences().stream().map(req -> {
                    ReskRequestedEvidence evidence = new ReskRequestedEvidence();
                    evidence.setAssessmentApplicantId(applicant.getId());
                    evidence.setUocCode(req.getUocCode());
                    evidence.setDetails(req.getDetails());
                    evidence.setRequestedByExaminerCode(examiner.getExaminerCode());
                    evidence.setRequestedAt(new Date());
                    evidence.setFulfilled(false);
                    return evidence;
                }).collect(Collectors.toList());

                requestedEvidenceRepository.saveAll(evidencesToSave);
            }
        }
    }

    private ReskUser getUserFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return reskUserRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("User not found: " + username));
    }

    private void validateExaminer(ReskUser user) {
        if (user.getExaminerCode() == null || user.getExaminerCode().isEmpty()) {
            throw new AccessDeniedException("User is not a valid examiner.");
        }
    }
}