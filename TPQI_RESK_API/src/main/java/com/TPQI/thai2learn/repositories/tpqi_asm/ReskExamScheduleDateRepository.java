package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.ReskExamScheduleDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReskExamScheduleDateRepository extends JpaRepository<ReskExamScheduleDate, Long> {
    Optional<ReskExamScheduleDate> findByTpqiExamNo(String tpqiExamNo);
}