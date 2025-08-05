package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.RelatedQualificationDTO;
import com.TPQI.thai2learn.DTO.UocDTO;
import com.TPQI.thai2learn.services.CompetencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/competencies")
public class CompetencyController {

    @Autowired
    private CompetencyService competencyService;

    @GetMapping("/by-exam-schedule/{examScheduleId}")
    public ResponseEntity<List<UocDTO>> getCompetencyTree(
            @PathVariable String examScheduleId) {
        
        List<UocDTO> competencyTree = competencyService.getCompetencyTreeByExamScheduleId(examScheduleId);

        if (competencyTree == null || competencyTree.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }

        return ResponseEntity.ok(competencyTree);
    }


    @GetMapping("/related-qualifications")
    public ResponseEntity<List<RelatedQualificationDTO>> getRelatedQualifications(
            @RequestParam("id") String examScheduleId) {
        
        List<RelatedQualificationDTO> qualifications = competencyService.getRelatedQualifications(examScheduleId);

        if (qualifications == null || qualifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(qualifications);
    }
}