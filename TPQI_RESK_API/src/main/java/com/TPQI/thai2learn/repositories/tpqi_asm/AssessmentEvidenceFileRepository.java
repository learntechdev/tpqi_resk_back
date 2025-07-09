package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentEvidenceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentEvidenceFileRepository extends JpaRepository<AssessmentEvidenceFile, Long> {
}