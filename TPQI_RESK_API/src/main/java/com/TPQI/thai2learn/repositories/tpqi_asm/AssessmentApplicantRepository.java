package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; 

@Repository
public interface AssessmentApplicantRepository extends JpaRepository<AssessmentApplicant, Long> {
    Optional<AssessmentApplicant> findByCitizenId(String citizenId);
    Optional<AssessmentApplicant> findByAppId(String appId);

}