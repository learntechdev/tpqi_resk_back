package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.CbExamRoundDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CBRepository {
    Page<CbApplicantSummaryDTO> findApplicantSummariesByExamRound(String tpqiExamNo, String search, String status, Pageable pageable);
    Page<CbExamRoundDTO> findExamRoundsByOrgCode(String orgCode, String search, Pageable pageable);
}