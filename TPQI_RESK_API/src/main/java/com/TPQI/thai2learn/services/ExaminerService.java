package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.repositories.tpqi_asm.AppointExaminerRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.ExaminerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExaminerService {

    private final ExaminerRepository examinerRepository;
    private final AppointExaminerRepository appointExaminerRepository;

    @Autowired
    public ExaminerService(ExaminerRepository examinerRepository,
                           AppointExaminerRepository appointExaminerRepository) {
        this.examinerRepository = examinerRepository;
        this.appointExaminerRepository = appointExaminerRepository;
    }

    @Transactional(readOnly = true)
    public Page<AssessmentInfoDTO> getExamRoundsForExaminer(
            String examinerCode, String search, Pageable pageable) {

        List<String> examCodes = appointExaminerRepository.findExamCodesByExaminerCode(examinerCode);
        if (examCodes.isEmpty()) {
            return Page.empty();
        }

        return examinerRepository.findExamRoundsByExamCodes(examCodes, search, pageable);
    }
}