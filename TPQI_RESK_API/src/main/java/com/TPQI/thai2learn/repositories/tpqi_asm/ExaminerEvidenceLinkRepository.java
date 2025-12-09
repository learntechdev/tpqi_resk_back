package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.ExaminerEvidenceLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExaminerEvidenceLinkRepository extends JpaRepository<ExaminerEvidenceLink, Long> {
    List<ExaminerEvidenceLink> findAllByAssessmentApplicantId(Long assessmentApplicantId);
    void deleteAllByAssessmentApplicantId(Long assessmentApplicantId);
}