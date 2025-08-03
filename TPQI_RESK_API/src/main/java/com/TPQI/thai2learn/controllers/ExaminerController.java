package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.ExaminerAssessmentDTO;
import com.TPQI.thai2learn.services.ExaminerService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.TPQI.thai2learn.DTO.AssessmentSubmissionPageDTO;
import com.TPQI.thai2learn.services.AssessmentSubmissionService;
import com.TPQI.thai2learn.DTO.EvidenceFileDTO;
import com.TPQI.thai2learn.services.FileStorageService;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/examiner")
public class ExaminerController {

    private final ExaminerService examinerService;
    private final AssessmentSubmissionService assessmentSubmissionService;
    private final FileStorageService fileStorageService;
    
    public ExaminerController(ExaminerService examinerService, 
                              AssessmentSubmissionService assessmentSubmissionService,
                              FileStorageService fileStorageService) {
        this.examinerService = examinerService;
        this.assessmentSubmissionService = assessmentSubmissionService;
        this.fileStorageService = fileStorageService;
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

    @PostMapping("/submit-assessment")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXAMINER')")
    public ResponseEntity<?> submitAssessment(
            Authentication authentication,
            @Valid @RequestBody ExaminerAssessmentDTO assessmentDTO) {
        
        examinerService.submitAssessment(authentication, assessmentDTO);
        return ResponseEntity.ok(Map.of("message", "Assessment submitted successfully."));
    }

    @GetMapping("/assessment-details/{applicantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXAMINER')")
    public ResponseEntity<AssessmentSubmissionPageDTO> getAssessmentDetailsForExaminer(@PathVariable Long applicantId) {
        AssessmentSubmissionPageDTO pageData = assessmentSubmissionService.getSubmissionPageDetails(applicantId);
        return ResponseEntity.ok(pageData);
    }

    @GetMapping("/files/{applicantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXAMINER')")
    public ResponseEntity<List<EvidenceFileDTO>> getFilesByApplicant(@PathVariable Long applicantId) {
        List<EvidenceFileDTO> files = fileStorageService.getFilesByApplicantId(applicantId);
        return ResponseEntity.ok(files);
    }

    @PostMapping("/upload-file")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXAMINER')")
    public ResponseEntity<?> uploadFileForApplicant(
            @RequestParam("file") MultipartFile file,
            @RequestParam("applicantId") Long applicantId,
            @RequestParam(value = "description", required = false) String description) {
        fileStorageService.storeAndSave(file, applicantId, description);
        return ResponseEntity.ok(Map.of("message", "File uploaded successfully"));
    }
}