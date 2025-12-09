package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.ExaminerAssessmentDTO;
import com.TPQI.thai2learn.DTO.ExaminerDraftDTO;
import com.TPQI.thai2learn.DTO.ExaminerFilterOptionsDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.*;
import com.TPQI.thai2learn.entities.tpqi_asm.types.AssessmentStatus;
import com.TPQI.thai2learn.repositories.tpqi_asm.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
    private final ExaminerEvidenceLinkRepository examinerEvidenceLinkRepository;

    public ExaminerService(ExaminerRepository examinerRepository,
                           AppointExaminerRepository appointExaminerRepository,
                           ReskUserRepository reskUserRepository,
                           CBRepository cbRepository,
                           AssessmentRepository assessmentRepository,
                           AssessmentApplicantRepository assessmentApplicantRepository,
                           AssessmentSubmissionDetailsRepository submissionDetailsRepository,
                           ReskRequestedEvidenceRepository requestedEvidenceRepository,
                           ExaminerEvidenceLinkRepository examinerEvidenceLinkRepository) {
        this.examinerRepository = examinerRepository;
        this.appointExaminerRepository = appointExaminerRepository;
        this.reskUserRepository = reskUserRepository;
        this.cbRepository = cbRepository;
        this.assessmentRepository = assessmentRepository;
        this.assessmentApplicantRepository = assessmentApplicantRepository;
        this.submissionDetailsRepository = submissionDetailsRepository;
        this.requestedEvidenceRepository = requestedEvidenceRepository;
        this.examinerEvidenceLinkRepository = examinerEvidenceLinkRepository;
    }

    @Transactional(readOnly = true)
    public Page<AssessmentInfoDTO> getExamRoundsForExaminer(Authentication authentication, String search, String qualification, String level, String tool, Pageable pageable) {
        ReskUser user = getUserFromAuthentication(authentication);
        validateExaminer(user);

        List<String> examCodes = appointExaminerRepository.findExamCodesByExaminerCode(user.getExaminerCode());
        if (examCodes.isEmpty()) {
            return Page.empty();
        }

        return examinerRepository.findExamRoundsByExamCodes(examCodes, search, qualification, level, tool, pageable);
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
        String resultText = mapCodeToThaiText(assessmentDTO.getResultStatus());

        saveOrUpdateAssessmentRecord(applicant, assessmentDTO, resultText);

        applicant.setAssessmentStatus(newStatus);
        assessmentApplicantRepository.save(applicant);

        updateSubmissionDetailsStatus(applicant.getId(), resultText);

        handleRequestedEvidences(examiner, applicant, assessmentDTO);
    }

    private AssessmentStatus mapStringToAssessmentStatus(String statusString) {
        return switch (statusString) {
            case "PASS" -> AssessmentStatus.EVALUATED_PASS;
            case "FAIL" -> AssessmentStatus.EVALUATED_FAIL;
            case "MORE_EVIDENCE" -> AssessmentStatus.MORE_EVIDENCE_REQUESTED;
            default -> throw new IllegalArgumentException("Invalid assessment status code: " + statusString);
        };
    }

    private String mapCodeToThaiText(String statusCode) {
        return switch (statusCode) {
            case "PASS" -> "ผ่าน";
            case "FAIL" -> "ไม่ผ่าน";
            case "MORE_EVIDENCE" -> "ขอหลักฐานเพิ่มเติม";
            default -> statusCode;
        };
    }

    private void saveOrUpdateAssessmentRecord(AssessmentApplicant applicant, ExaminerAssessmentDTO assessmentDTO, String resultText) {
        Optional<Assessment> existingAssessment = assessmentRepository.findByAppId(applicant.getAppId());
        Assessment assessment = existingAssessment.orElse(new Assessment());

        assessment.setAppId(applicant.getAppId());
        assessment.setExamScheduleId(applicant.getExamScheduleId());
        assessment.setCitizenId(applicant.getCitizenId());
        assessment.setToolType(applicant.getAsmToolType());
        assessment.setAssessmentDate(new Date());
        assessment.setExamResult(resultText);
        assessment.setRecomment(assessmentDTO.getComments());
        assessment.setTotalScore(0);
        assessment.setFullScore(0);
        assessment.setExamPercentScore(BigDecimal.ZERO);
        assessment.setType("2");
        assessmentRepository.save(assessment);
    }

    private void updateSubmissionDetailsStatus(Long applicantId, String resultText) {
        AssessmentSubmissionDetails submissionDetails = submissionDetailsRepository.findByAssessmentApplicantId(applicantId)
                .orElse(new AssessmentSubmissionDetails());
        submissionDetails.setAssessmentApplicantId(applicantId);
        submissionDetails.setSubmissionStatus(resultText);
        submissionDetailsRepository.save(submissionDetails);
    }

    private void handleRequestedEvidences(ReskUser examiner, AssessmentApplicant applicant, ExaminerAssessmentDTO assessmentDTO) {
        if ("MORE_EVIDENCE".equals(assessmentDTO.getResultStatus())) {
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

    @Transactional
    public void saveAssessmentDraft(Authentication authentication, ExaminerDraftDTO draftDTO) {
        Long applicantId = draftDTO.getApplicantId();
        ReskUser examiner = getUserFromAuthentication(authentication);
        validateExaminer(examiner);

        AssessmentApplicant applicant = assessmentApplicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found: " + applicantId));

        examinerEvidenceLinkRepository.deleteAllByAssessmentApplicantId(applicantId);
        if (draftDTO.getEvidenceLinks() != null && !draftDTO.getEvidenceLinks().isEmpty()) {
            List<ExaminerEvidenceLink> newLinks = new ArrayList<>();
            for (var linkDTO : draftDTO.getEvidenceLinks()) {
                if (linkDTO.getCompetencyCodes() == null) continue;
                for (String competencyCode : linkDTO.getCompetencyCodes()) {
                    ExaminerEvidenceLink link = new ExaminerEvidenceLink();
                    link.setAssessmentApplicantId(applicantId);
                    link.setEvidenceFileId(linkDTO.getFileId());
                    link.setCompetencyCode(competencyCode);
                    newLinks.add(link);
                }
            }
            if (!newLinks.isEmpty()) {
                examinerEvidenceLinkRepository.saveAll(newLinks);
            }
        }

        Assessment assessment = assessmentRepository.findByAppId(applicant.getAppId())
                .orElseGet(() -> {
                    Assessment a = new Assessment();
                    a.setAppId(applicant.getAppId());
                    a.setExamScheduleId(applicant.getExamScheduleId());
                    a.setCitizenId(applicant.getCitizenId());
                    a.setAssessmentDate(new Date());
                    a.setTotalScore(0);
                    a.setFullScore(0);
                    a.setExamPercentScore(BigDecimal.ZERO);
                    a.setType("2");
                    return a;
                });
        assessment.setRecomment(draftDTO.getComments());
        if (draftDTO.getResultStatus() != null && !draftDTO.getResultStatus().isBlank()) {
            assessment.setExamResult(mapCodeToThaiText(draftDTO.getResultStatus()));
        } else if (assessment.getExamResult() == null) {
            assessment.setExamResult("Draft");
        }
        assessmentRepository.save(assessment);

        requestedEvidenceRepository.deleteAllByAssessmentApplicantId(applicantId);
        if ("MORE_EVIDENCE".equals(draftDTO.getResultStatus())
                && draftDTO.getRequestedEvidences() != null
                && !draftDTO.getRequestedEvidences().isEmpty()) {

            List<ReskRequestedEvidence> toSave = draftDTO.getRequestedEvidences().stream()
                    .map(req -> {
                        ReskRequestedEvidence e = new ReskRequestedEvidence();
                        e.setAssessmentApplicantId(applicantId);
                        e.setUocCode(req.getUocCode());
                        e.setDetails(req.getDetails());
                        e.setRequestedByExaminerCode(examiner.getExaminerCode());
                        e.setRequestedAt(new Date());
                        e.setFulfilled(false);
                        return e;
                    })
                    .collect(Collectors.toList());
            requestedEvidenceRepository.saveAll(toSave);
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

    @Transactional(readOnly = true)
    public ExaminerFilterOptionsDTO getFilterOptionsForExaminer(Authentication authentication) {
        ReskUser user = getUserFromAuthentication(authentication);
        validateExaminer(user);

        List<String> examCodes = appointExaminerRepository.findExamCodesByExaminerCode(user.getExaminerCode());
        if (examCodes.isEmpty()) {
            return new ExaminerFilterOptionsDTO();
        }

        List<String> allOccLevelNames = examinerRepository.findDistinctOccLevelNamesByExamCodes(examCodes);
        List<String> allTools = examinerRepository.findDistinctAssessmentToolsByExamCodes(examCodes);

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
}
