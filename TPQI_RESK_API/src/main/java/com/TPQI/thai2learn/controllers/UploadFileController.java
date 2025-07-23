package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.EvidenceFileDTO;
import com.TPQI.thai2learn.services.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentEvidenceFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class UploadFileController {

    private final FileStorageService fileStorageService;

    public UploadFileController(
            FileStorageService fileStorageService
    ) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("applicantId") Long applicantId,
            @RequestParam(value = "description", required = false) String description) {

        try {
            AssessmentEvidenceFile savedFile = fileStorageService.storeAndSave(file, applicantId, description);

            Map<String, Object> response = Map.of(
                    "message", "File uploaded successfully!",
                    "fileId", savedFile.getId(),
                    "filePath", savedFile.getFilePath()
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = Map.of("message", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }

    @GetMapping("/by-applicant/{applicantId}")
    public ResponseEntity<List<EvidenceFileDTO>> getFilesByApplicant(@PathVariable Long applicantId) {
        List<EvidenceFileDTO> files = fileStorageService.getFilesByApplicantId(applicantId);
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId) {
        try {
            fileStorageService.deleteFile(fileId);
            return ResponseEntity.ok(Map.of("message", "File deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to delete file: " + e.getMessage()));
        }
    }
}