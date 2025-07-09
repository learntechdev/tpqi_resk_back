package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssessmentRepository {
    Page<AssessmentInfoDTO> findAssessmentInfoByAppId(String appId, String search, Pageable pageable);
}