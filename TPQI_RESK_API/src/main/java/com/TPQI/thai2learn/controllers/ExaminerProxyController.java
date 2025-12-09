package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.services.ExaminerProxyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.TPQI.thai2learn.DTO.ProxyAssessmentDTO;
import org.springframework.web.multipart.MultipartFile;
import com.TPQI.thai2learn.DTO.ExaminerFilterOptionsDTO;
import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.ExaminerInfoDTO;
import com.TPQI.thai2learn.DTO.UocDTO;
import com.TPQI.thai2learn.services.CompetencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proxy")
@PreAuthorize("hasRole('EXAMINER_PROXY')")
public class ExaminerProxyController {

    private final ExaminerProxyService examinerProxyService;
    private final CompetencyService competencyService;
    private final ObjectMapper objectMapper;

    public ExaminerProxyController(ExaminerProxyService examinerProxyService, CompetencyService competencyService, ObjectMapper objectMapper) {
        this.examinerProxyService = examinerProxyService;
        this.competencyService = competencyService;
        this.objectMapper = objectMapper;
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
        Page<AssessmentInfoDTO> result = examinerProxyService.getAllExamRounds(authentication, search, qualification, level, tool, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/submit-assessment", consumes = {"multipart/form-data"})
    public ResponseEntity<?> submitProxyAssessment(
            Authentication authentication,
            @RequestPart("assessment") String assessmentJson,
            @RequestPart("delegationFiles") List<MultipartFile> delegationFiles) {
        
        try {
            ProxyAssessmentDTO assessmentDTO = objectMapper.readValue(assessmentJson, ProxyAssessmentDTO.class);
            examinerProxyService.submitProxyAssessment(authentication, assessmentDTO, delegationFiles);
            return ResponseEntity.ok(Map.of("message", "Proxy assessment submitted successfully."));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid assessment data or failed to process request.", "details", e.getMessage()));
        }
    }

    @GetMapping("/exam-rounds/filters")
    public ResponseEntity<ExaminerFilterOptionsDTO> getExamRoundFilters(Authentication authentication) {
        ExaminerFilterOptionsDTO filterOptions = examinerProxyService.getFilterOptions(authentication);
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
        Page<CbApplicantSummaryDTO> applicantsPage = examinerProxyService.getApplicantsByExamRound(authentication, tpqiExamNo, search, status, pageable);
        return ResponseEntity.ok(applicantsPage);
    }

    @GetMapping("/examiners-by-round")
    public ResponseEntity<List<ExaminerInfoDTO>> getExaminersByExamRound(@RequestParam String tpqiExamNo) {
        List<ExaminerInfoDTO> examiners = examinerProxyService.getExaminersByExamRound(tpqiExamNo);
        return ResponseEntity.ok(examiners);
    }

    @GetMapping("/competency-tree")
    public ResponseEntity<List<UocDTO>> getCompetencyTree(@RequestParam String tpqiExamNo) {
        List<UocDTO> competencyTree = competencyService.getCompetencyTreeByExamScheduleId(tpqiExamNo);
        if (competencyTree == null || competencyTree.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(competencyTree);
    }
}