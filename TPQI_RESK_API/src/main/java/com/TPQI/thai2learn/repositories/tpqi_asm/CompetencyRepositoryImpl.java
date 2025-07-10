package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.EocDTO;
import com.TPQI.thai2learn.DTO.PcDTO;
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
        String sql = "SELECT occ_level_id FROM exam_schedule WHERE exam_schedule_id = :examScheduleId LIMIT 1";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("examScheduleId", examScheduleId);
        try {
            Object result = query.getSingleResult();
            return Long.parseLong(result.toString());
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
}