package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentEvidenceFile; 
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentEvidenceFileRepository; 
import com.TPQI.thai2learn.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class UploadFileController {

    private final FileStorageService fileStorageService;
    private final AssessmentEvidenceFileRepository evidenceFileRepository; 

    @Autowired
    public UploadFileController(
        FileStorageService fileStorageService,
        AssessmentEvidenceFileRepository evidenceFileRepository 
    ) {
        this.fileStorageService = fileStorageService;
        this.evidenceFileRepository = evidenceFileRepository; 
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("applicantId") Long applicantId,
            @RequestParam("evidenceType") String evidenceType) {

        try {
            String generatedFileName = fileStorageService.store(file);
            String filePath = "/uploads/" + generatedFileName;
  
            AssessmentEvidenceFile evidenceFile = new AssessmentEvidenceFile();
            evidenceFile.setAssessmentApplicantId(applicantId);
            evidenceFile.setEvidenceType(evidenceType);
            evidenceFile.setFilePath(filePath);
            evidenceFile.setOriginalFilename(file.getOriginalFilename());
    
            evidenceFileRepository.save(evidenceFile);

            Map<String, String> response = Map.of(
                "message", "File uploaded and data saved successfully!",
                "filePath", filePath
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = Map.of("message", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }
}