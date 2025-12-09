package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.AssessmentSubmissionPageDTO;
import com.TPQI.thai2learn.DTO.UnlinkedUocDTO;
import com.TPQI.thai2learn.services.AssessmentSubmissionService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AssessmentSubmissionController {

    @Autowired
    private AssessmentSubmissionService assessmentSubmissionService;

    @GetMapping("/assessment-submission-details/{id}")
    public ResponseEntity<AssessmentSubmissionPageDTO> getSubmissionDetails(@PathVariable("id") Long assessmentApplicantId) {
        try {
            AssessmentSubmissionPageDTO pageData = assessmentSubmissionService.getSubmissionPageDetails(assessmentApplicantId);
            return ResponseEntity.ok(pageData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/assessment-submission-details/{id}/unlinked-competencies")
    public ResponseEntity<List<UnlinkedUocDTO>> getUnlinkedCompetencies(@PathVariable("id") Long assessmentApplicantId) {
        try {
            List<UnlinkedUocDTO> unlinked = assessmentSubmissionService.getUnlinkedCompetencyTree(assessmentApplicantId);
            return ResponseEntity.ok(unlinked);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}