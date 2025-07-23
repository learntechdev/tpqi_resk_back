package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentPriorCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssessmentPriorCertificateRepository extends JpaRepository<AssessmentPriorCertificate, Long> {
    void deleteAllByAssessmentApplicantId(Long assessmentApplicantId);

    List<AssessmentPriorCertificate> findAllByAssessmentApplicantId(Long assessmentApplicantId);
}