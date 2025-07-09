package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Transactional(readOnly = true)
    public Page<AssessmentInfoDTO> getAssessmentsByAppId(String appId, String search, Pageable pageable) {
        if (appId == null || appId.trim().isEmpty()) {
            return Page.empty(); 
        }
        return assessmentRepository.findAssessmentInfoByAppId(appId, search, pageable);
    }
}