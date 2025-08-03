package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class AssessmentRepositoryImpl implements AssessmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<AssessmentInfoDTO> findAssessmentInfoByAppId(String appId, String search, Pageable pageable) {
        String fromClause = """
            FROM
                assessment_applicant aa
            LEFT JOIN
                exam_schedule es ON aa.exam_schedule_id = es.tpqi_exam_no
            LEFT JOIN
                settings_tooltype st ON aa.asm_tool_type = st.id
            LEFT JOIN
                assessment a ON aa.app_id = a.app_id
            LEFT JOIN
                resk_exam_schedule_dates rsd ON aa.exam_schedule_id = rsd.tpqi_exam_no
            """;
        StringBuilder whereClause = new StringBuilder(" WHERE aa.app_id = :appId ");
        if (search != null && !search.trim().isEmpty()) {
            whereClause.append(" AND ( aa.exam_schedule_id LIKE :search OR es.org_name LIKE :search OR es.occ_level_name LIKE :search OR st.tooltype_name LIKE :search OR es.place LIKE :search ) ");
        }
        String countSql = "SELECT COUNT(*) " + fromClause + whereClause.toString();
        Query countQuery = entityManager.createNativeQuery(countSql);
        countQuery.setParameter("appId", appId);
        if (search != null && !search.trim().isEmpty()) {
            countQuery.setParameter("search", "%" + search + "%");
        }
        long total = ((Number) countQuery.getSingleResult()).longValue();
        String dataSql = """
            SELECT
                aa.id,
                aa.exam_schedule_id,
                es.org_name,
                es.occ_level_name,
                st.tooltype_name,
                es.place,
                rsd.actual_exam_date,
                a.assessment_date
            """ + fromClause + whereClause.toString() + " ORDER BY rsd.actual_exam_date DESC, aa.id DESC";
        Query dataQuery = entityManager.createNativeQuery(dataSql, Object.class);
        dataQuery.setParameter("appId", appId);
        if (search != null && !search.trim().isEmpty()) {
            dataQuery.setParameter("search", "%" + search + "%");
        }
        dataQuery.setFirstResult((int) pageable.getOffset());
        dataQuery.setMaxResults(pageable.getPageSize());
        List<Object[]> results = dataQuery.getResultList();
        List<AssessmentInfoDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            AssessmentInfoDTO dto = new AssessmentInfoDTO();
            if (row[0] != null) { dto.setId(((Number) row[0]).longValue()); }
            dto.setExamRound((String) row[1]);
            dto.setCertifyingBody((String) row[2]);
            String occLevelName = (String) row[3];
            dto.setAssessmentTool((String) row[4]);
            dto.setAssessmentPlace((String) row[5]);
            dto.setExamDate((Date) row[6]);
            dto.setAssessmentDate((Date) row[7]);
            if (occLevelName != null && !occLevelName.isEmpty()) {
                 try {
                    String textToParse = occLevelName;
                    String[] levelParts = textToParse.split("ระดับ", 2);
                    if (levelParts.length > 1) { dto.setLevel(levelParts[1].trim()); textToParse = levelParts[0].trim(); }
                    String[] occupationParts = textToParse.split("อาชีพ", 2);
                    if (occupationParts.length > 1) { dto.setOccupation(occupationParts[1].trim()); textToParse = occupationParts[0].trim(); }
                    int lastBranchIndex = textToParse.lastIndexOf("สาขา");
                    if (lastBranchIndex > 0) {
                        dto.setProfession(textToParse.substring(0, lastBranchIndex).trim());
                        dto.setBranch(textToParse.substring(lastBranchIndex).trim());
                    } else {
                        dto.setProfession(textToParse.trim());
                    }
                 } catch (Exception e) {
                    System.err.println("Could not parse occ_level_name: " + occLevelName + "; Error: " + e.getMessage());
                 }
            } else {
                dto.setProfession(occLevelName);
                dto.setBranch("ไม่มีสาขา");
            }
            dtos.add(dto);
        }
        return new PageImpl<>(dtos, pageable, total);
    }

}