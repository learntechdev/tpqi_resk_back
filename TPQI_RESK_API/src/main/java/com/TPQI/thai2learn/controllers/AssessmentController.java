package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assessments") 
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;


    @GetMapping("/by-app/{appId}")
    public ResponseEntity<List<AssessmentInfoDTO>> getMyAssessments(@PathVariable String appId) {
        List<AssessmentInfoDTO> assessments = assessmentService.getAssessmentsByAppId(appId);
        if (assessments.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(assessments); 
    }
}