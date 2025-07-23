package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.TPQI.thai2learn.DTO.AssessmentFilterOptionsDTO;

@RestController
@RequestMapping("/api/assessments") 
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;


    @GetMapping("/by-app/{appId}")
    public ResponseEntity<Page<AssessmentInfoDTO>> getMyAssessments(
            @PathVariable String appId,
            @RequestParam(required = false) String search, 
            @RequestParam(defaultValue = "0") int page,     
            @RequestParam(defaultValue = "10") int size      
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AssessmentInfoDTO> assessmentsPage = assessmentService.getAssessmentsByAppId(appId, search, pageable);
        
        if (assessmentsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(assessmentsPage);
    }

     @GetMapping("/filters/{appId}")
    public ResponseEntity<AssessmentFilterOptionsDTO> getAssessmentFilterOptions(@PathVariable String appId) { // <--- เพิ่ม @PathVariable
        AssessmentFilterOptionsDTO filterOptions = assessmentService.getFilterOptionsByAppId(appId); // <--- เรียกใช้ method ใหม่
        return ResponseEntity.ok(filterOptions);
    }
}