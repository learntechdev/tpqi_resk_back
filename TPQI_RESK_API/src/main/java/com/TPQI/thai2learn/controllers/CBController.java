package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.AssessmentSubmissionPageDTO;
import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.EvidenceFileDTO;
import com.TPQI.thai2learn.DTO.SubmissionRequestDTO;
import com.TPQI.thai2learn.services.AssessmentSubmissionService;
import com.TPQI.thai2learn.services.CBService;
import com.TPQI.thai2learn.services.FileStorageService;
import com.TPQI.thai2learn.services.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cb")
public class CBController {

    @Autowired
    private CBService cbService;

    @Autowired
    private AssessmentSubmissionService assessmentSubmissionService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/applicants")
    @PreAuthorize("hasRole('cb_officer')")
    public ResponseEntity<?> getApplicants(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CbApplicantSummaryDTO> applicantsPage = cbService.getApplicantSummaries(search, status, pageable);
        return ResponseEntity.ok(applicantsPage);
    }

    @GetMapping("/submission-statuses")
    @PreAuthorize("hasRole('cb_officer')")
    public ResponseEntity<List<String>> getSubmissionStatuses() {
        List<String> statuses = List.of(
            "ยังไม่ได้ประเมิน", "ยังไม่ส่งประเมิน", "ส่งประเมินแล้ว",
            "เจ้าหน้าที่สอบขอหลักฐานเพิ่ม", "ประเมินผลแล้ว", "ยกเลิกผลการประเมินแล้ว"
        );
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/submission-details/{applicantId}")
    @PreAuthorize("hasRole('cb_officer')")
    public ResponseEntity<?> getSubmissionDetailsForCb(@PathVariable Long applicantId) {
        AssessmentSubmissionPageDTO pageData = assessmentSubmissionService.getSubmissionPageDetails(applicantId);
        return ResponseEntity.ok(pageData);
    }

    @GetMapping("/files/{applicantId}")
    @PreAuthorize("hasRole('cb_officer')")
    public ResponseEntity<List<EvidenceFileDTO>> getFilesByApplicant(@PathVariable Long applicantId) {
        List<EvidenceFileDTO> files = fileStorageService.getFilesByApplicantId(applicantId);
        return ResponseEntity.ok(files);
    }

    @PostMapping("/upload-file")
    @PreAuthorize("hasRole('cb_officer')")
    public ResponseEntity<?> uploadFileForApplicant(
            @RequestParam("file") MultipartFile file,
            @RequestParam("applicantId") Long applicantId,
            @RequestParam(value = "description", required = false) String description) {
        fileStorageService.storeAndSave(file, applicantId, description);
        return ResponseEntity.ok(Map.of("message", "File uploaded successfully for applicant: " + applicantId));
    }

    @DeleteMapping("/delete-file/{fileId}")
    @PreAuthorize("hasRole('cb_officer')")
    public ResponseEntity<?> deleteFileForApplicant(@PathVariable Long fileId) {
        fileStorageService.deleteFile(fileId);
        return ResponseEntity.ok(Map.of("message", "File deleted successfully!"));
    }

    @PostMapping("/save-submission")
    @PreAuthorize("hasRole('cb_officer')")
    public ResponseEntity<?> saveSubmissionForApplicant(@RequestBody SubmissionRequestDTO submissionRequest) {
        submissionService.processSubmission(submissionRequest);
        return ResponseEntity.ok().body(Map.of("message", "Submission saved successfully!"));
    }
}