package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentSubmissionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssessmentSubmissionDetailsRepository extends JpaRepository<AssessmentSubmissionDetails, Long> {
    Optional<AssessmentSubmissionDetails> findByAssessmentApplicantId(Long assessmentApplicantId);
}