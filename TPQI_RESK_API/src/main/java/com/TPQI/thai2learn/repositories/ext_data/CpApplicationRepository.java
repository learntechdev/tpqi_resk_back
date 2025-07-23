package com.TPQI.thai2learn.repositories.ext_data;

import com.TPQI.thai2learn.entities.ext_data.CpApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CpApplicationRepository extends JpaRepository<CpApplication, Integer> {
    List<CpApplication> findByPersonId(int personId);
    Optional<CpApplication> findByApplicationCode(String applicationCode);
}