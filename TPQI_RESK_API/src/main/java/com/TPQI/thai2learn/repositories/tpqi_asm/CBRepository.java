package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CBRepository {
    Page<CbApplicantSummaryDTO> findApplicantSummaries(String orgCode, String search, String status, Pageable pageable);
}