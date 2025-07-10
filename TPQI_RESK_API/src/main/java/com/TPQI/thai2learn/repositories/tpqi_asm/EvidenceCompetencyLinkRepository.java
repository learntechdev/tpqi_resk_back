package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.EvidenceCompetencyLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvidenceCompetencyLinkRepository extends JpaRepository<EvidenceCompetencyLink, Long> {
    void deleteAllByAssessmentApplicantId(Long assessmentApplicantId);
}