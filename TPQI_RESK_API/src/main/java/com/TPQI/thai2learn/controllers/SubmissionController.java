package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.SubmissionRequestDTO;
import com.TPQI.thai2learn.services.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/submission")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping("/save")
    public ResponseEntity<?> saveSubmission(@RequestBody SubmissionRequestDTO submissionRequest) {
        try {

            submissionService.processSubmission(submissionRequest);
            return ResponseEntity.ok().body(Map.of("message", "Submission saved successfully!"));
            
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to save submission: " + e.getMessage()));
        }
    }
}