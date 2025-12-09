package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExaminerRepository {
    Page<AssessmentInfoDTO> findExamRoundsByExamCodes(List<String> examCodes, String search, String qualification, String level, String tool, Pageable pageable);

    Page<AssessmentInfoDTO> findAllExamRounds(String search, String qualification, String level, String tool, Pageable pageable);

    List<String> findAllDistinctOccLevelNames();
    List<String> findAllDistinctAssessmentTools();

    List<String> findDistinctOccLevelNamesByExamCodes(List<String> examCodes);
    List<String> findDistinctAssessmentToolsByExamCodes(List<String> examCodes);
}