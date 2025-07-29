package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.CbExamRoundDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentApplicant;
import com.TPQI.thai2learn.entities.tpqi_asm.ExamSchedule;
import com.TPQI.thai2learn.entities.tpqi_asm.ReskUser;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentApplicantRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.CBRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.ExamScheduleRepository; // 💡 1. เพิ่ม Import
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CBService {

    @Autowired
    private CBRepository cbRepository;

    @Autowired
    private ReskUserRepository reskUserRepository;

    @Autowired
    private ExamScheduleRepository examScheduleRepository;

    @Autowired
    private AssessmentApplicantRepository assessmentApplicantRepository;

    @Transactional(readOnly = true)
    public Page<CbExamRoundDTO> getExamRoundsForCbUser(Authentication authentication, String search, Pageable pageable) {
        ReskUser user = getUserFromAuthentication(authentication);
        validateCbOfficer(user);
        return cbRepository.findExamRoundsByOrgCode(user.getOrgCode(), search, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CbApplicantSummaryDTO> getApplicantsByExamRound(Authentication authentication, String tpqiExamNo, String search, String status, Pageable pageable) {
        ReskUser user = getUserFromAuthentication(authentication);
        validateCbOfficer(user);

        ExamSchedule examSchedule = examScheduleRepository.findByTpqiExamNo(tpqiExamNo);
        if (examSchedule == null || !examSchedule.getOrgId().equals(user.getOrgCode())) {
            throw new AccessDeniedException("You do not have permission to view applicants for this exam round.");
        }

        return cbRepository.findApplicantSummariesByExamRound(tpqiExamNo, search, status, pageable);
    }

    private ReskUser getUserFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return reskUserRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("User not found: " + username));
    }

    private void validateCbOfficer(ReskUser user) {
        if (user.getOrgCode() == null || user.getOrgCode().isEmpty()) {
            throw new AccessDeniedException("User is not a valid CB officer or does not have an assigned organization code.");
        }
    }

    public void verifyApplicantAccess(Authentication authentication, Long applicantId) {
    ReskUser cbUser = getUserFromAuthentication(authentication);
    validateCbOfficer(cbUser);

    AssessmentApplicant applicant = assessmentApplicantRepository.findById(applicantId)
            .orElseThrow(() -> new RuntimeException("Applicant not found with id: " + applicantId));

    ExamSchedule examSchedule = examScheduleRepository.findByTpqiExamNo(applicant.getExamScheduleId());

    if (examSchedule == null || !examSchedule.getOrgId().equals(cbUser.getOrgCode())) {
        throw new AccessDeniedException("You do not have permission to access this applicant's data.");
    }
}
}