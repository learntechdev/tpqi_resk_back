package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long>, AssessmentRepositoryCustom {
    Optional<Assessment> findByAppId(String appId);
}