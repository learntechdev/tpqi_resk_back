package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.CbApplicantSummaryDTO;
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
    public Page<CbApplicantSummaryDTO> findApplicantSummaries(String search, String status, Pageable pageable) {
        
        String selectClause = "SELECT " +
                              "aa.id, aa.app_id, aa.initial_name, aa.name, aa.lastname, aa.citizen_id, " +
                              "asd.submission_status, " +
                              "es.start_date, " +
                              "a.assessment_date ";

        String fromClause = "FROM assessment_applicant aa " +
                            "LEFT JOIN assessment_submission_details asd ON aa.id = asd.assessment_applicant_id " +
                            "LEFT JOIN exam_schedule es ON aa.exam_schedule_id = es.tpqi_exam_no " +
                            "LEFT JOIN assessment a ON aa.app_id = a.app_id ";

        StringBuilder whereClause = new StringBuilder("WHERE 1=1 ");
        if (search != null && !search.trim().isEmpty()) {
            whereClause.append("AND (CONCAT(aa.name, ' ', aa.lastname) LIKE :search OR aa.citizen_id LIKE :search) ");
        }
        if (status != null && !status.trim().isEmpty()) {
            whereClause.append("AND asd.submission_status = :status ");
        }

        String orderByClause = "ORDER BY aa.id DESC ";

        String dataSql = selectClause + fromClause + whereClause.toString() + orderByClause + "LIMIT :limit OFFSET :offset";
        Query dataQuery = entityManager.createNativeQuery(dataSql);
        dataQuery.setParameter("limit", pageable.getPageSize());
        dataQuery.setParameter("offset", pageable.getOffset());

        if (search != null && !search.trim().isEmpty()) {
            dataQuery.setParameter("search", "%" + search + "%");
        }
        if (status != null && !status.trim().isEmpty()) {
            dataQuery.setParameter("status", status);
        }
        
        List<Object[]> results = dataQuery.getResultList();
        
        List<CbApplicantSummaryDTO> dtos = results.stream().map(row -> {
            CbApplicantSummaryDTO dto = new CbApplicantSummaryDTO();
            dto.setApplicantId(((Number) row[0]).longValue());
            dto.setAppId((String) row[1]);
            
            // --- แก้ไขบรรทัดนี้ ---
            // แปลง Object เป็น String ก่อนเรียกใช้ .trim()
            String initialName = row[2] != null ? ((String) row[2]).trim() : "";
            String firstName = row[3] != null ? ((String) row[3]).trim() : "";
            String lastName = row[4] != null ? ((String) row[4]).trim() : "";
            String fullName = initialName + firstName + " " + lastName;
            // --- จบส่วนแก้ไข ---
            
            dto.setFullName(fullName.trim());
            dto.setCitizenId((String) row[5]);
            dto.setSubmissionStatus(row[6] != null ? (String) row[6] : "ยังไม่เริ่ม");
            dto.setExamDate(row[7] != null ? (Date) row[7] : null);
            dto.setAssessmentDate(row[8] != null ? (Date) row[8] : null);
            return dto;
        }).collect(Collectors.toList());
        
        String countSql = "SELECT COUNT(aa.id) " + fromClause + whereClause.toString();
        Query countQuery = entityManager.createNativeQuery(countSql);

        if (search != null && !search.trim().isEmpty()) {
            countQuery.setParameter("search", "%" + search + "%");
        }
        if (status != null && !status.trim().isEmpty()) {
            countQuery.setParameter("status", status);
        }

        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(dtos, pageable, total);
    }
}