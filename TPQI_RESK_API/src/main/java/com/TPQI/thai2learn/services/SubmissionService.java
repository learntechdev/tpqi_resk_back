package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.EvidenceLinkDTO;
import com.TPQI.thai2learn.DTO.PriorCertificateDTO;
import com.TPQI.thai2learn.DTO.SubmissionRequestDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentPriorCertificate;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentSubmissionDetails;
import com.TPQI.thai2learn.entities.tpqi_asm.EvidenceCompetencyLink;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentApplicantRepository; // Repo ที่ห้ามลบ
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentPriorCertificateRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentSubmissionDetailsRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.EvidenceCompetencyLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private EvidenceCompetencyLinkRepository evidenceCompetencyLinkRepository;
    
    @Autowired
    private AssessmentPriorCertificateRepository priorCertificateRepository;

    @Autowired
    private AssessmentSubmissionDetailsRepository submissionDetailsRepository;
    
    @Autowired
    private AssessmentApplicantRepository assessmentApplicantRepository; // Repo ที่ห้ามลบ

    @Transactional
    public void processSubmission(SubmissionRequestDTO request) {
        Long applicantId = request.getAssessmentApplicantId();
        if (applicantId == null) {
            throw new IllegalArgumentException("Assessment Applicant ID cannot be null.");
        }

        assessmentApplicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("Applicant not found with id: " + applicantId));

        AssessmentSubmissionDetails details = submissionDetailsRepository.findByAssessmentApplicantId(applicantId)
                .orElse(new AssessmentSubmissionDetails());

        details.setAssessmentApplicantId(applicantId);
        details.setExperienceYears(request.getExperienceYears());
        details.setSubmissionStatus(request.getSubmissionStatus());
        submissionDetailsRepository.save(details);

        priorCertificateRepository.deleteAllByAssessmentApplicantId(applicantId);
        evidenceCompetencyLinkRepository.deleteAllByAssessmentApplicantId(applicantId);

        if (Boolean.TRUE.equals(request.getHasPriorCertificate()) && request.getPriorCertificates() != null) {
            List<AssessmentPriorCertificate> priorCertsToSave = new ArrayList<>();
            for (PriorCertificateDTO certDTO : request.getPriorCertificates()) {
                AssessmentPriorCertificate newCert = new AssessmentPriorCertificate();
                newCert.setAssessmentApplicantId(applicantId);
                newCert.setQualificationId(certDTO.getQualificationId());
                newCert.setEvidenceFileId(certDTO.getFileId());
                priorCertsToSave.add(newCert);
            }
            if (!priorCertsToSave.isEmpty()) {
                priorCertificateRepository.saveAll(priorCertsToSave);
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
}