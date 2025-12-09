package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.*;
import com.TPQI.thai2learn.entities.tpqi_asm.*;
import com.TPQI.thai2learn.entities.tpqi_asm.types.AssessmentStatus;
import com.TPQI.thai2learn.repositories.tpqi_asm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    @Autowired
    private EvidenceCompetencyLinkRepository evidenceCompetencyLinkRepository;
    @Autowired
    private AssessmentPriorCertificateRepository priorCertificateRepository;
    @Autowired
    private AssessmentSubmissionDetailsRepository submissionDetailsRepository;
    @Autowired
    private AssessmentApplicantRepository assessmentApplicantRepository;
    @Autowired
    private CompetencyRepository competencyRepository;
    @Autowired
    private CompetencyService competencyService;
    @Autowired
    private AssessmentEvidenceFileRepository evidenceFileRepository;

    @Transactional
    public void processSubmission(SubmissionRequestDTO request) {
        Long applicantId = request.getAssessmentApplicantId();
        if (applicantId == null) {
            throw new IllegalArgumentException("Assessment Applicant ID cannot be null.");
        }

        AssessmentApplicant applicant = assessmentApplicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("Applicant not found with id: " + applicantId));

        if ("SUBMITTED".equalsIgnoreCase(request.getSubmissionStatus())) {
        validateSubmission(applicant, request.getEvidenceLinks());
        applicant.setAssessmentStatus(AssessmentStatus.SUBMITTED);
        assessmentApplicantRepository.save(applicant);
        } else if ("DRAFT".equalsIgnoreCase(request.getSubmissionStatus())) {
            if (applicant.getAssessmentStatus() == null || applicant.getAssessmentStatus() == AssessmentStatus.JUST_START) {
                applicant.setAssessmentStatus(AssessmentStatus.PENDING_SUBMISSION);
                assessmentApplicantRepository.save(applicant);
            }
        }


        AssessmentSubmissionDetails details = submissionDetailsRepository.findByAssessmentApplicantId(applicantId)
                .orElse(new AssessmentSubmissionDetails());
        details.setAssessmentApplicantId(applicantId);
        details.setExperienceYears(request.getExperienceYears());
        details.setSubmissionStatus(request.getSubmissionStatus());
        submissionDetailsRepository.save(details);

        priorCertificateRepository.deleteAllByAssessmentApplicantId(applicantId);
        evidenceCompetencyLinkRepository.deleteAllByAssessmentApplicantId(applicantId);

        if (Boolean.TRUE.equals(request.getHasPriorCertificate()) && request.getPriorCertificates() != null) {
            List<AssessmentPriorCertificate> priorCertsToSave = request.getPriorCertificates().stream().map(certDTO -> {
                AssessmentPriorCertificate newCert = new AssessmentPriorCertificate();
                newCert.setAssessmentApplicantId(applicantId);
                newCert.setQualificationId(certDTO.getQualificationId());
                newCert.setEvidenceFileId(certDTO.getFileId());
                return newCert;
            }).collect(Collectors.toList());
            if (!priorCertsToSave.isEmpty()) {
                priorCertificateRepository.saveAll(priorCertsToSave);
            }
        }

        if (request.getEvidenceDetails() != null) {
            for (EvidenceDetailDTO detail : request.getEvidenceDetails()) {
                evidenceFileRepository.findById(detail.getFileId()).ifPresent(file -> {
                    file.setEvidenceTypes(detail.getEvidenceTypes());
                    evidenceFileRepository.save(file);
                });
            }
        }

        if (request.getEvidenceLinks() != null) {
            List<EvidenceCompetencyLink> newLinks = new ArrayList<>();
            for (EvidenceLinkDTO linkDTO : request.getEvidenceLinks()) {
                for (String competencyCode : linkDTO.getCompetencyCodes()) {
                    EvidenceCompetencyLink newLink = new EvidenceCompetencyLink();
                    newLink.setAssessmentApplicantId(applicantId);
                    newLink.setEvidenceFileId(linkDTO.getFileId());
                    newLink.setCompetencyCode(competencyCode);
                    newLinks.add(newLink);
                }
            }
            if (!newLinks.isEmpty()) {
                evidenceCompetencyLinkRepository.saveAll(newLinks);
            }
        }
    }

    private void validateSubmission(AssessmentApplicant applicant, List<EvidenceLinkDTO> evidenceLinks) {
        String tpqiExamNo = applicant.getExamScheduleId();
        Long examSchedulePkId = competencyRepository.findExamScheduleIdByTpqiExamNo(tpqiExamNo);
        if (examSchedulePkId == null) {
            throw new IllegalStateException("ไม่สามารถหารอบสอบสำหรับ validation ได้");
        }

        List<UocDTO> requiredUocs = competencyService.getCompetencyTreeByExamScheduleId(String.valueOf(examSchedulePkId));
        Set<String> requiredUocCodes = requiredUocs.stream().map(UocDTO::getUocCode).collect(Collectors.toSet());

        if (evidenceLinks == null || evidenceLinks.isEmpty()) {
            throw new IllegalArgumentException("กรุณาแนบหลักฐานให้ครบทุกหน่วยสมรรถนะก่อนยืนยันการส่ง");
        }

        Set<String> linkedUocCodes = evidenceLinks.stream()
                .flatMap(link -> link.getCompetencyCodes().stream())
                .filter(code -> requiredUocCodes.contains(code))
                .collect(Collectors.toSet());

        if (!linkedUocCodes.containsAll(requiredUocCodes)) {
             throw new IllegalArgumentException("ยังไม่ได้แนบหลักฐานสำหรับบางหน่วยสมรรถนะ กรุณาตรวจสอบและแนบหลักฐานให้ครบถ้วน");
        }
    }
}