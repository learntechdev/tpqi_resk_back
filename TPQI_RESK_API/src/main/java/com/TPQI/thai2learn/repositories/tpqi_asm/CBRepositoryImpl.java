package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
import com.TPQI.thai2learn.DTO.CbExamRoundDTO;

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
        LEFT JOIN assessment_submission_details asd ON aa.id = asd.assessment_applicant_id
        LEFT JOIN exam_schedule es ON aa.exam_schedule_id = es.tpqi_exam_no
        LEFT JOIN assessment a ON aa.app_id = a.app_id
    """;

    String whereClause = " WHERE aa.exam_schedule_id = :tpqiExamNo ";
    if (search != null && !search.trim().isEmpty()) {
        whereClause += " AND (CONCAT(aa.name, ' ', aa.lastname) LIKE :search OR aa.citizen_id LIKE :search) ";
    }
    if (status != null && !status.trim().isEmpty()) {
        whereClause += " AND asd.submission_status = :status ";
    }

    String countSql = "SELECT COUNT(aa.id) " + fromClause + whereClause;
    Query countQuery = entityManager.createNativeQuery(countSql);
    countQuery.setParameter("tpqiExamNo", tpqiExamNo);
    if (search != null && !search.trim().isEmpty()) {
        countQuery.setParameter("search", "%" + search + "%");
    }
    if (status != null && !status.trim().isEmpty()) {
        countQuery.setParameter("status", status);
    }
    long total = ((Number) countQuery.getSingleResult()).longValue();

    String dataSql = "SELECT aa.id, aa.app_id, aa.initial_name, aa.name, aa.lastname, aa.citizen_id, " +
                     "asd.submission_status, es.start_date, a.assessment_date " +
                     fromClause + whereClause + " ORDER BY aa.id DESC";

    Query dataQuery = entityManager.createNativeQuery(dataSql);
    dataQuery.setParameter("tpqiExamNo", tpqiExamNo);
    if (search != null && !search.trim().isEmpty()) {
        dataQuery.setParameter("search", "%" + search + "%");
    }
    if (status != null && !status.trim().isEmpty()) {
        dataQuery.setParameter("status", status);
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
        dto.setSubmissionStatus(row[6] != null ? (String) row[6] : "ยังไม่เริ่ม");
        dto.setExamDate(row[7] != null ? (Date) row[7] : null);
        dto.setAssessmentDate(row[8] != null ? (Date) row[8] : null);
        return dto;
    }).collect(Collectors.toList());

    return new PageImpl<>(dtos, pageable, total);
}

    @Override
    public Page<CbExamRoundDTO> findExamRoundsByOrgCode(String orgCode, String search, Pageable pageable) {
        
        String fromAndWhereClause = """
            FROM exam_schedule es
            WHERE es.org_id = :orgCode
        """;
        if (search != null && !search.trim().isEmpty()) {
            fromAndWhereClause += " AND (es.tpqi_exam_no LIKE :search OR es.occ_level_name LIKE :search OR es.place LIKE :search) ";
        }

        String countSql = "SELECT COUNT(DISTINCT es.exam_schedule_id) " + fromAndWhereClause;
        Query countQuery = entityManager.createNativeQuery(countSql);
        countQuery.setParameter("orgCode", orgCode);
        if (search != null && !search.trim().isEmpty()) {
            countQuery.setParameter("search", "%" + search + "%");
        }
        long total = ((Number) countQuery.getSingleResult()).longValue();

        String dataSql = """
            SELECT DISTINCT
                es.exam_schedule_id,
                es.tpqi_exam_no,
                es.occ_level_name,
                (SELECT st.tooltype_name FROM settings_tooltype st JOIN assessment_applicant aa ON st.id = aa.asm_tool_type WHERE aa.exam_schedule_id = es.tpqi_exam_no LIMIT 1) AS assessment_tool,
                es.place,
                es.start_date,
                (SELECT a.assessment_date FROM assessment a JOIN assessment_applicant aa ON a.app_id = aa.app_id WHERE aa.exam_schedule_id = es.tpqi_exam_no LIMIT 1) AS assessment_date,
                (SELECT COUNT(app.id) FROM assessment_applicant app WHERE app.exam_schedule_id = es.tpqi_exam_no) as applicant_count
        """ + fromAndWhereClause + " ORDER BY es.start_date DESC";

        Query dataQuery = entityManager.createNativeQuery(dataSql);
        dataQuery.setParameter("orgCode", orgCode);
        if (search != null && !search.trim().isEmpty()) {
            dataQuery.setParameter("search", "%" + search + "%");
        }
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

            String profession = occLevelName;
            String branch = "ไม่มีสาขา";
            String occupation = null;
            String level = null;
            
            if (occLevelName != null && !occLevelName.isEmpty()) {
                try {
                    String textToParse = occLevelName;
                    String[] levelParts = textToParse.split("ระดับ", 2);
                    if (levelParts.length > 1) {
                        level = levelParts[1].trim();
                        textToParse = levelParts[0].trim();
                    }
                    String[] occupationParts = textToParse.split("อาชีพ", 2);
                    if (occupationParts.length > 1) {
                        occupation = occupationParts[1].trim();
                        textToParse = occupationParts[0].trim();
                    }
                    int lastBranchIndex = textToParse.lastIndexOf("สาขา");
                    if (lastBranchIndex > 0) {
                        profession = textToParse.substring(0, lastBranchIndex).trim();
                        branch = textToParse.substring(lastBranchIndex).trim();
                    } else {
                        profession = textToParse.trim();
                    }
                } catch (Exception e) {
                }
            }

            return new CbExamRoundDTO(examScheduleId, tpqiExamNo, profession, branch, occupation, level, assessmentTool, assessmentPlace, examDate, assessmentDate, applicantCount);

        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, total);
    }
}