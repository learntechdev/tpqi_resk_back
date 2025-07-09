package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Transactional(readOnly = true)
    public List<AssessmentInfoDTO> getAssessmentsByAppId(String appId) {
        if (appId == null || appId.trim().isEmpty()) {
            return Collections.emptyList(); 
        }
        return assessmentRepository.findAssessmentInfoByAppId(appId);
    }
}