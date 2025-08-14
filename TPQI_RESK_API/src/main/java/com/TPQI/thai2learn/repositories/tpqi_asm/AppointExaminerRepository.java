package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.AppointExaminer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointExaminerRepository extends JpaRepository<AppointExaminer, Integer> {

    @Query("SELECT ae.tpqiExamNo FROM AppointExaminer ae WHERE ae.examinerCode = :examinerCode")
    List<String> findExamCodesByExaminerCode(String examinerCode);

    List<AppointExaminer> findByTpqiExamNo(String tpqiExamNo);

    boolean existsByTpqiExamNoAndExaminerCode(String tpqiExamNo, String examinerCode);
}