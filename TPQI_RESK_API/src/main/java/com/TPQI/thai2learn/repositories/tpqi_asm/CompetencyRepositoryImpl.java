package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.EocDTO;
import com.TPQI.thai2learn.DTO.PcDTO;
import com.TPQI.thai2learn.DTO.RelatedQualificationDTO;
import com.TPQI.thai2learn.DTO.UocDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("unchecked")
public class CompetencyRepositoryImpl implements CompetencyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long findOccLevelIdByExamScheduleId(String examScheduleId) {
        Long pkId;
        try {
            pkId = Long.parseLong(examScheduleId);
        } catch (NumberFormatException e) {
            return null;
        }

        String sql = "SELECT occ_level_id FROM exam_schedule WHERE exam_schedule_id = :examScheduleId LIMIT 1";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("examScheduleId", pkId);
        try {
            Object result = query.getSingleResult();
            return result != null ? Long.parseLong(result.toString()) : null;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<UocDTO> findUocsByOccLevelId(Long occLevelId) {
        String sql = "SELECT uoc_id, uoc_code, uoc_name FROM standard_uoc WHERE occ_level_id = :occLevelId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("occLevelId", occLevelId);
        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(row -> new UocDTO(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2],
                        new ArrayList<>()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<EocDTO> findEocsByUocIds(List<String> uocIds) {
        if (uocIds == null || uocIds.isEmpty()) return new ArrayList<>();
        String sql = "SELECT uoc_id, eoc_id, eoc_code, eoc_name FROM standard_eoc WHERE uoc_id IN (:uocIds)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("uocIds", uocIds);
        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(row -> {
                    EocDTO dto = new EocDTO(
                            (String) row[1],
                            (String) row[2],
                            (String) row[3],
                            new ArrayList<>()
                    );
                    dto.setParentUocId((String) row[0]);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PcDTO> findPcsByEocIds(List<String> eocIds) {
        if (eocIds == null || eocIds.isEmpty()) return new ArrayList<>();
        String sql = "SELECT eoc_id, pc_id, pc_name FROM standard_pc WHERE eoc_id IN (:eocIds)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("eocIds", eocIds);
        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(row -> {
                    PcDTO dto = new PcDTO(
                            (String) row[1],
                            (String) row[2]
                    );
                    dto.setParentEocId((String) row[0]);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public String findTier2TitleByOccLevelId(Long occLevelId) {
        String sql = "SELECT tier2_title FROM standard_qualification WHERE id = :occLevelId LIMIT 1";
        Query query = entityManager.createNativeQuery(sql, String.class);
        query.setParameter("occLevelId", occLevelId);
        try {
            return (String) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<RelatedQualificationDTO> findRelatedQualificationsByTier2Title(String tier2Title, Long excludeId) {
        String sql = "SELECT id, tier1_title, tier2_title, tier3_title, level_name " +
                    "FROM standard_qualification " +
                    "WHERE tier2_title = :tier2Title AND status = '1' AND id != :excludeId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("tier2Title", tier2Title);
        query.setParameter("excludeId", excludeId);
        List<Object[]> results = query.getResultList();

        return results.stream().map(row -> {
            String name = String.join(" ",
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    (String) row[4]
            ).replaceAll("\\s+", " ").trim();
            return new RelatedQualificationDTO(((Number) row[0]).longValue(), name);
        }).collect(Collectors.toList());
    }
    
    @Override
    public Long findExamScheduleIdByTpqiExamNo(String tpqiExamNo) {
        String sql = "SELECT exam_schedule_id FROM exam_schedule WHERE tpqi_exam_no = :tpqiExamNo LIMIT 1";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("tpqiExamNo", tpqiExamNo);
        try {
            Object result = query.getSingleResult();
            return result != null ? Long.parseLong(result.toString()) : null;
        } catch (NoResultException e) {
            return null;
        }
    }
}