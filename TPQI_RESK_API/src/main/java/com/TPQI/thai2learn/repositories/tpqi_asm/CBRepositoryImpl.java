package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.CbExamRoundDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.types.AssessmentStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CBRepositoryImpl implements CBRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CbApplicantSummaryDTO> findApplicantSummariesByExamRound(String tpqiExamNo, String search, String status, Pageable pageable) {

        String fromClause = """
            FROM assessment_applicant aa
            LEFT JOIN exam_schedule es ON aa.exam_schedule_id = es.tpqi_exam_no
            LEFT JOIN assessment a ON aa.app_id = a.app_id
            LEFT JOIN resk_exam_schedule_dates rsd ON aa.exam_schedule_id = rsd.tpqi_exam_no
        """;

        StringBuilder whereClause = new StringBuilder(" WHERE aa.exam_schedule_id = :tpqiExamNo ");

        if (search != null && !search.trim().isEmpty()) {
            whereClause.append(" AND (CONCAT(aa.initial_name, aa.name, ' ', aa.lastname) LIKE :search OR aa.citizen_id LIKE :search) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            boolean isValidStatus = true;
            switch (status) {
                case "ยังไม่ได้ประเมิน":
                    whereClause.append(" AND aa.assessment_status = ").append(AssessmentStatus.JUST_START.getCode());
                    break;
                case "ยังไม่ส่งประเมิน":
                    whereClause.append(" AND aa.assessment_status = ").append(AssessmentStatus.PENDING_SUBMISSION.getCode());
                    break;
                case "ส่งประเมินแล้ว":
                    whereClause.append(" AND aa.assessment_status = ").append(AssessmentStatus.SUBMITTED.getCode());
                    break;
                case "เจ้าหน้าที่สอบขอหลักฐานเพิ่ม":
                    whereClause.append(" AND aa.assessment_status = ").append(AssessmentStatus.MORE_EVIDENCE_REQUESTED.getCode());
                    break;
                case "ประเมินผลแล้ว":
                    whereClause.append(" AND aa.assessment_status IN (")
                            .append(AssessmentStatus.EVALUATED_PASS.getCode()).append(", ")
                            .append(AssessmentStatus.EVALUATED_FAIL.getCode()).append(") ");
                    break;
                case "ยกเลิกผลการประเมินแล้ว":
                    whereClause.append(" AND aa.assessment_status = ").append(AssessmentStatus.CANCELLED.getCode());
                    break;
                default:
                    isValidStatus = false;
                    break;
            }

            if (!isValidStatus) {
                whereClause.append(" AND 1=0 ");
            }
        }

        String countSql = "SELECT COUNT(DISTINCT aa.id) " + fromClause + whereClause;
        Query countQuery = entityManager.createNativeQuery(countSql);
        countQuery.setParameter("tpqiExamNo", tpqiExamNo);

        if (search != null && !search.trim().isEmpty()) {
            countQuery.setParameter("search", "%" + search + "%");
        }
        
        long total = ((Number) countQuery.getSingleResult()).longValue();

        String dataSql = "SELECT DISTINCT aa.id, aa.app_id, aa.initial_name, aa.name, aa.lastname, aa.citizen_id, " +
                        "aa.assessment_status, rsd.actual_exam_date, a.assessment_date " +
                        fromClause + whereClause + " ORDER BY aa.id DESC";

        Query dataQuery = entityManager.createNativeQuery(dataSql);
        dataQuery.setParameter("tpqiExamNo", tpqiExamNo);

        if (search != null && !search.trim().isEmpty()) {
            dataQuery.setParameter("search", "%" + search + "%");
        }

        dataQuery.setFirstResult((int) pageable.getOffset());
        dataQuery.setMaxResults(pageable.getPageSize());

        List<Object[]> results = dataQuery.getResultList();
        List<CbApplicantSummaryDTO> dtos = results.stream().map(row -> {
            CbApplicantSummaryDTO dto = new CbApplicantSummaryDTO();
            dto.setApplicantId(((Number) row[0]).longValue());
            dto.setAppId((String) row[1]);
            String initialName = row[2] != null ? ((String) row[2]).trim() : "";
            String firstName = row[3] != null ? ((String) row[3]).trim() : "";
            String lastName = row[4] != null ? ((String) row[4]).trim() : "";
            dto.setFullName((initialName + firstName + " " + lastName).trim());
            dto.setCitizenId((String) row[5]);
            
            String statusDisplay = "ยังไม่ได้ประเมิน";
            if (row[6] != null) {
                try {
                    int sCode = Integer.parseInt(row[6].toString());
                    statusDisplay = AssessmentStatus.of(sCode).getDisplayName();
                } catch (IllegalArgumentException e) {}
            }
            dto.setSubmissionStatus(statusDisplay);
            
            dto.setExamDate(row[7] != null ? (Date) row[7] : null);
            dto.setAssessmentDate(row[8] != null ? (Date) row[8] : null);
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, total);
    }
    
    @Override
    public Page<CbExamRoundDTO> findExamRoundsByOrgCode(String orgCode, String search, String qualification, String level, String tool, Pageable pageable) {

        String fromAndWhereClause = """
            FROM exam_schedule es
            LEFT JOIN resk_exam_schedule_dates rsd ON es.tpqi_exam_no = rsd.tpqi_exam_no
            LEFT JOIN (SELECT DISTINCT exam_schedule_id, asm_tool_type FROM assessment_applicant) aa ON es.tpqi_exam_no = aa.exam_schedule_id
            LEFT JOIN settings_tooltype st ON aa.asm_tool_type = st.id
            WHERE es.org_id = :orgCode
        """;

        StringBuilder dynamicWhere = new StringBuilder(fromAndWhereClause);
        if (search != null && !search.trim().isEmpty()) {
            dynamicWhere.append(" AND (es.tpqi_exam_no LIKE :search OR es.occ_level_name LIKE :search OR es.place LIKE :search) ");
        }
        if (qualification != null && !qualification.trim().isEmpty()) {
            dynamicWhere.append(" AND es.occ_level_name LIKE :qualification ");
        }
        if (level != null && !level.trim().isEmpty()) {
            dynamicWhere.append(" AND es.occ_level_name LIKE :level ");
        }
        if (tool != null && !tool.trim().isEmpty()) {
            dynamicWhere.append(" AND st.tooltype_name = :tool ");
        }

        String countSql = "SELECT COUNT(DISTINCT es.exam_schedule_id) " + dynamicWhere.toString();
        Query countQuery = entityManager.createNativeQuery(countSql);
        countQuery.setParameter("orgCode", orgCode);
        if (search != null && !search.trim().isEmpty()) countQuery.setParameter("search", "%" + search + "%");
        if (qualification != null && !qualification.trim().isEmpty()) countQuery.setParameter("qualification", qualification + "%"); // 💡 แก้ไข
        if (level != null && !level.trim().isEmpty()) countQuery.setParameter("level", "%ระดับ " + level); // 💡 แก้ไข
        if (tool != null && !tool.trim().isEmpty()) countQuery.setParameter("tool", tool);

        long total = ((Number) countQuery.getSingleResult()).longValue();

        String dataSql = """
            SELECT DISTINCT
                es.exam_schedule_id, es.tpqi_exam_no, es.occ_level_name, st.tooltype_name,
                es.place, rsd.actual_exam_date, rsd.actual_assessment_date,
                (SELECT COUNT(app.id) FROM assessment_applicant app WHERE app.exam_schedule_id = es.tpqi_exam_no) as applicant_count
        """ + dynamicWhere.toString() + " GROUP BY es.exam_schedule_id ORDER BY rsd.actual_exam_date DESC"; // 💡 เพิ่ม GROUP BY

        Query dataQuery = entityManager.createNativeQuery(dataSql);
        dataQuery.setParameter("orgCode", orgCode);
        if (search != null && !search.trim().isEmpty()) dataQuery.setParameter("search", "%" + search + "%");
        if (qualification != null && !qualification.trim().isEmpty()) dataQuery.setParameter("qualification", qualification + "%"); // 💡 แก้ไข
        if (level != null && !level.trim().isEmpty()) dataQuery.setParameter("level", "%ระดับ " + level); // 💡 แก้ไข
        if (tool != null && !tool.trim().isEmpty()) dataQuery.setParameter("tool", tool);

        dataQuery.setFirstResult((int) pageable.getOffset());
        dataQuery.setMaxResults(pageable.getPageSize());
        List<Object[]> results = dataQuery.getResultList();
        List<CbExamRoundDTO> dtos = results.stream().map(row -> {
            Long examScheduleId = row[0] != null ? ((Number) row[0]).longValue() : null;
            String tpqiExamNo = (String) row[1];
            String occLevelName = (String) row[2];
            String assessmentTool = (String) row[3];
            String assessmentPlace = (String) row[4];
            Date examDate = (Date) row[5];
            Date assessmentDate = (Date) row[6];
            Long applicantCount = row[7] != null ? ((Number) row[7]).longValue() : 0L;
            
            String parsedProfession = occLevelName;
            String parsedBranch = "ไม่มีสาขา";
            String parsedOccupation = null;
            String parsedLevel = null;
            
            if (occLevelName != null && !occLevelName.isEmpty()) {
                try {
                    String textToParse = occLevelName;
                    String[] levelParts = textToParse.split("ระดับ", 2);
                    if (levelParts.length > 1) {
                        parsedLevel = levelParts[1].trim();
                        textToParse = levelParts[0].trim();
                    }
                    String[] occupationParts = textToParse.split("อาชีพ", 2);
                    if (occupationParts.length > 1) {
                        parsedOccupation = occupationParts[1].trim();
                        textToParse = occupationParts[0].trim();
                    }
                    int lastBranchIndex = textToParse.lastIndexOf("สาขา");
                    if (lastBranchIndex > 0) {
                        parsedProfession = textToParse.substring(0, lastBranchIndex).trim();
                        parsedBranch = textToParse.substring(lastBranchIndex).trim();
                    } else {
                        parsedProfession = textToParse.trim();
                    }
                } catch (Exception e) {
                }
            }
            
            return new CbExamRoundDTO(examScheduleId, tpqiExamNo, parsedProfession, parsedBranch, parsedOccupation, parsedLevel, assessmentTool, assessmentPlace, examDate, assessmentDate, applicantCount);

        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, total);
    }

    @Override
    public List<String> findDistinctOccLevelNamesByOrgCode(String orgCode) {
        String sql = """
            SELECT DISTINCT es.occ_level_name 
            FROM exam_schedule es 
            WHERE es.org_id = :orgCode 
            AND es.occ_level_name IS NOT NULL AND es.occ_level_name != ''
            ORDER BY es.occ_level_name
        """;
        Query query = entityManager.createNativeQuery(sql, String.class);
        query.setParameter("orgCode", orgCode);
        return query.getResultList();
    }

    @Override
    public List<String> findDistinctAssessmentToolsByOrgCode(String orgCode) {
        String sql = """
            SELECT DISTINCT st.tooltype_name 
            FROM assessment_applicant aa
            JOIN settings_tooltype st ON aa.asm_tool_type = st.id
            WHERE aa.org_id = :orgCode
            AND st.tooltype_name IS NOT NULL AND st.tooltype_name != ''
            ORDER BY st.tooltype_name
        """;
        Query query = entityManager.createNativeQuery(sql, String.class);
        query.setParameter("orgCode", orgCode);
        return query.getResultList();
    }
}