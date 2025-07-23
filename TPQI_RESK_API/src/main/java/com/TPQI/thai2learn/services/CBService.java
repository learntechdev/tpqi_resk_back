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

    // แก้ไข method ให้รับ search และ status
    public Page<CbApplicantSummaryDTO> getApplicantSummaries(String search, String status, Pageable pageable) {
        // ส่งต่อไปให้ Repository
        return cbRepository.findApplicantSummaries(search, status, pageable);
    }
}