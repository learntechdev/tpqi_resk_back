package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.CbExamRoundDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentApplicant;
import com.TPQI.thai2learn.entities.tpqi_asm.ExamSchedule;
import com.TPQI.thai2learn.entities.tpqi_asm.ReskUser;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentApplicantRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.CBRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.ExamScheduleRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.TPQI.thai2learn.DTO.CbFilterOptionsDTO;
import java.util.*;

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
    public Page<CbExamRoundDTO> getExamRoundsForCbUser(Authentication authentication, String search, String qualification, String level, String tool, Pageable pageable) {
        ReskUser user = getUserFromAuthentication(authentication);
        validateCbOfficer(user);
        return cbRepository.findExamRoundsByOrgCode(user.getOrgCode(), search, qualification, level, tool, pageable);
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

    @Transactional(readOnly = true)
    public CbFilterOptionsDTO getFilterOptionsForCbUser(Authentication authentication) {
        ReskUser user = getUserFromAuthentication(authentication);
        validateCbOfficer(user);
        String orgCode = user.getOrgCode();

        CbFilterOptionsDTO options = new CbFilterOptionsDTO();
        List<String> allOccLevelNames = cbRepository.findDistinctOccLevelNamesByOrgCode(orgCode);

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

        options.setAssessmentTools(cbRepository.findDistinctAssessmentToolsByOrgCode(orgCode));
        options.setStatuses(List.of(
            "ยังไม่ได้ประเมิน", "ยังไม่ส่งประเมิน", "ส่งประเมินแล้ว",
            "เจ้าหน้าที่สอบขอหลักฐานเพิ่ม", "ประเมินผลแล้ว", "ยกเลิกผลการประเมินแล้ว"
        ));

        return options;
    }
}