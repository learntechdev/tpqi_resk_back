package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentEvidenceFile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentEvidenceFileRepository extends JpaRepository<AssessmentEvidenceFile, Long> {
    List<AssessmentEvidenceFile> findByAssessmentApplicantId(Long assessmentApplicantId);
}