package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.ExaminerFilterOptionsDTO;
import com.TPQI.thai2learn.services.AuditorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.TPQI.thai2learn.DTO.AssessmentCancellationDTO;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auditor")
@PreAuthorize("hasRole('AUDITOR')")
public class AuditorController {

    private final AuditorService auditorService;

    public AuditorController(AuditorService auditorService) {
        this.auditorService = auditorService;
    }

    @GetMapping("/exam-rounds")
    public ResponseEntity<Page<AssessmentInfoDTO>> getAllExamRounds(
            Authentication authentication,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String qualification,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String tool,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<AssessmentInfoDTO> result = auditorService.getAllExamRounds(authentication, search, qualification, level, tool, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/exam-rounds/filters")
    public ResponseEntity<ExaminerFilterOptionsDTO> getExamRoundFilters(Authentication authentication) {
        ExaminerFilterOptionsDTO filterOptions = auditorService.getFilterOptions(authentication);
        return ResponseEntity.ok(filterOptions);
    }

    @GetMapping("/applicants")
    public ResponseEntity<Page<CbApplicantSummaryDTO>> getApplicantsByExamRound(
            @RequestParam String tpqiExamNo,
            Authentication authentication,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<CbApplicantSummaryDTO> applicantsPage = auditorService.getApplicantsByExamRound(authentication, tpqiExamNo, search, status, pageable);
        return ResponseEntity.ok(applicantsPage);
    }

    @PostMapping("/cancel-assessment")
    public ResponseEntity<?> cancelAssessment(
            @Valid @RequestBody AssessmentCancellationDTO cancellationDTO,
            Authentication authentication) {
        
        auditorService.cancelAssessment(authentication, cancellationDTO);
        return ResponseEntity.ok(Map.of("message", "Assessment has been cancelled successfully."));
    }
}