package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.ReskRequestedEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ReskRequestedEvidenceRepository extends JpaRepository<ReskRequestedEvidence, Long> {

    void deleteAllByAssessmentApplicantId(Long assessmentApplicantId);
    List<ReskRequestedEvidence> findAllByAssessmentApplicantId(Long assessmentApplicantId);
}