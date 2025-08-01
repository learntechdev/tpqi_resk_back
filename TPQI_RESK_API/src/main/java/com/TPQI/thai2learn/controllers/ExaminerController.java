package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.services.ExaminerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/examiner")
public class ExaminerController {

    private final ExaminerService examinerService;


    public ExaminerController(ExaminerService examinerService) {
        this.examinerService = examinerService;
    }

    @GetMapping("/exam-rounds")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXAMINER')")
    public ResponseEntity<Page<AssessmentInfoDTO>> getExamRounds(
            Authentication authentication,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AssessmentInfoDTO> result = examinerService.getExamRoundsForExaminer(authentication, search, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/exam-rounds/{tpqiExamNo}/applicants")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXAMINER')")
    public ResponseEntity<Page<CbApplicantSummaryDTO>> getApplicantsByExamRound(
            @PathVariable String tpqiExamNo,
            Authentication authentication,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CbApplicantSummaryDTO> applicantsPage = examinerService.getApplicantsByExamRound(authentication, tpqiExamNo, search, status, pageable);
        return ResponseEntity.ok(applicantsPage);
    }
}