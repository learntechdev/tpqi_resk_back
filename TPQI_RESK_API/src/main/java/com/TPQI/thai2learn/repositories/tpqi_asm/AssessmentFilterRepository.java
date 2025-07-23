package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentFilterRepository extends JpaRepository<AssessmentApplicant, Long> {

    @Query(value = "SELECT DISTINCT es.occ_level_name FROM assessment_applicant aa " +
                   "JOIN exam_schedule es ON aa.exam_schedule_id = es.exam_schedule_id " +
                   "WHERE aa.app_id = :appId " +
                   "AND es.occ_level_name IS NOT NULL AND es.occ_level_name != '' " +
                   "ORDER BY es.occ_level_name", nativeQuery = true)
    List<String> findDistinctOccLevelNamesByAppId(String appId); 

    @Query(value = "SELECT DISTINCT st.tooltype_name FROM assessment_applicant aa " +
                   "JOIN settings_tooltype st ON aa.asm_tool_type = st.id " +
                   "WHERE aa.app_id = :appId " + 
                   "AND st.tooltype_name IS NOT NULL AND st.tooltype_name != '' " +
                   "ORDER BY st.tooltype_name", nativeQuery = true)
    List<String> findDistinctAssessmentToolsByAppId(String appId); 
}