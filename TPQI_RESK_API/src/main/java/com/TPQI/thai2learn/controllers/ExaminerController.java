package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.ReskUser;
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskUserRepository;
import com.TPQI.thai2learn.services.ExaminerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/examiner")
@PreAuthorize("hasRole('EXAMINER')")
public class ExaminerController {

    private final ExaminerService examinerService;
    private final ReskUserRepository reskUserRepository;

    @Autowired
    public ExaminerController(ExaminerService examinerService, ReskUserRepository reskUserRepository) {
        this.examinerService = examinerService;
        this.reskUserRepository = reskUserRepository;
    }

    @GetMapping("/exam-rounds")
    public ResponseEntity<?> getExamRounds(
            Authentication authentication,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String username = authentication.getName();
        ReskUser user = reskUserRepository.findByUsername(username)
                .orElse(null);

        if (user == null || user.getExaminerCode() == null || user.getExaminerCode().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(Map.of("error", "User is not a valid examiner."));
        }
        
        String examinerCode = user.getExaminerCode();

        Pageable pageable = PageRequest.of(page, size);
        Page<AssessmentInfoDTO> result = examinerService.getExamRoundsForExaminer(examinerCode, search, pageable);
        
        return ResponseEntity.ok(result);
    }
}