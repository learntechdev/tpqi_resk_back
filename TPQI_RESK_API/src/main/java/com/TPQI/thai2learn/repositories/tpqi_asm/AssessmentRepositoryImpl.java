package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class AssessmentRepositoryImpl implements AssessmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AssessmentInfoDTO> findAssessmentInfoByAppId(String appId) {
        String sql = """
            SELECT
                aa.exam_schedule_id,
                es.org_name,
                es.occ_level_name,
                st.tooltype_name,
                es.place,
                es.start_date,
                es.end_date
            FROM
                assessment_applicant aa
            LEFT JOIN
                exam_schedule es ON aa.exam_schedule_id = es.exam_schedule_id
            LEFT JOIN
                settings_tooltype st ON aa.asm_tool_type = st.id
            WHERE
                aa.app_id = :appId
        """;

        Query query = entityManager.createNativeQuery(sql, Object[].class);
        query.setParameter("appId", appId);

        List<Object[]> results = query.getResultList();
        List<AssessmentInfoDTO> dtos = new ArrayList<>();

        for (Object[] row : results) {
            AssessmentInfoDTO dto = new AssessmentInfoDTO();

            dto.setExamRound((String) row[0]);

            dto.setCertifyingBody((String) row[1]);

            String occLevelName = (String) row[2];
            dto.setProfession(occLevelName);
            dto.setBranch("ไม่มีสาขา");
            dto.setOccupation(null);
            dto.setLevel(null);

            if (occLevelName != null && !occLevelName.isEmpty()) {
                 try {
                    String textToParse = occLevelName;
                    String[] levelParts = textToParse.split("ระดับ", 2);
                    if (levelParts.length > 1) {
                        dto.setLevel(levelParts[1].trim());
                        textToParse = levelParts[0].trim();
                    }
                    String[] occupationParts = textToParse.split("อาชีพ", 2);
                    if (occupationParts.length > 1) {
                        dto.setOccupation(occupationParts[1].trim());
                        textToParse = occupationParts[0].trim();
                    }
                    int lastBranchIndex = textToParse.lastIndexOf("สาขา");
                    if (lastBranchIndex > 0) {
                        dto.setProfession(textToParse.substring(0, lastBranchIndex).trim());
                        dto.setBranch(textToParse.substring(lastBranchIndex).trim());
                    } else {
                        dto.setProfession(textToParse.trim());
                    }
                 } catch (Exception e) {
                    System.err.println("Could not parse occ_level_name: " + occLevelName);
                 }
            }

            dto.setAssessmentTool((String) row[3]);
            dto.setAssessmentPlace((String) row[4]);

            if (row[5] instanceof Date) {
                 dto.setExamDate((Date) row[5]);
            }
            if (row[6] instanceof Date) {
                 dto.setAssessmentDate((Date) row[6]);
            }

            dtos.add(dto);
        }

        return dtos;
    }
}