package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExaminerRepository {
    Page<AssessmentInfoDTO> findExamRoundsByExamCodes(List<String> examCodes, String search, Pageable pageable);
}