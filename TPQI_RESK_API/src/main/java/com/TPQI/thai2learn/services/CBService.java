package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.repositories.tpqi_asm.CBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CBService {

    @Autowired
    private CBRepository cbRepository;

    public Page<CbApplicantSummaryDTO> getApplicantSummaries(String orgCode, String search, String status, Pageable pageable) {
        return cbRepository.findApplicantSummaries(orgCode, search, status, pageable);
    }
}