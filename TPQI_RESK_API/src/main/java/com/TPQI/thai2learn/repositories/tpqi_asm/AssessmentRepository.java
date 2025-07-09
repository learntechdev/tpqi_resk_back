package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import java.util.List;


public interface AssessmentRepository {
    List<AssessmentInfoDTO> findAssessmentInfoByAppId(String appId);
}