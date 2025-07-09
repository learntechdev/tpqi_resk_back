package com.TPQI.thai2learn.repositories.ext_data;

import com.TPQI.thai2learn.entities.ext_data.CpApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CpApplicationRepository extends JpaRepository<CpApplication, Integer> {
    // method ชื่อ findBy + ชื่อ field camelCase ตาม entity (personId)
    List<CpApplication> findByPersonId(int personId);
}