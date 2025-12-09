package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UocHierarchyService {

    @PersistenceContext(unitName = "mysqlEntityManagerFactory")
    private EntityManager entityManager;

    public List<UocDTO> getHierarchyByOccLevelId(String occLevelId) {
        List<Object[]> uocRows = entityManager.createNativeQuery("""
            SELECT id, uoc_id, uoc_code, uoc_name 
            FROM tpqinet_asm_uat.standard_uoc1 
            WHERE occ_level_id = :occ
        """).setParameter("occ", occLevelId).getResultList();

        List<Object[]> eocRows = entityManager.createNativeQuery("""
            SELECT id, eoc_id, eoc_code, eoc_name, uoc_id 
            FROM tpqinet_asm_uat.standard_eoc1 
            WHERE occ_level_id = :occ
        """).setParameter("occ", occLevelId).getResultList();

        List<Object[]> pcRows = entityManager.createNativeQuery("""
            SELECT id, eoc_id, pc_id, pc_name 
            FROM tpqinet_asm_uat.standard_pc1 
            WHERE occ_level_id = :occ
        """).setParameter("occ", occLevelId).getResultList();

        Map<String, List<PcDTO>> pcMap = pcRows.stream().map(row -> {
            PcDTO dto = new PcDTO();
            dto.setEocId((String) row[1]);
            dto.setPcId((String) row[2]);
            dto.setPcName((String) row[3]);
            return dto;
        }).collect(Collectors.groupingBy(PcDTO::getEocId));

        Map<String, List<EocDTO>> eocMap = eocRows.stream().map(row -> {
            EocDTO dto = new EocDTO();
            dto.setEocId((String) row[1]);
            dto.setEocCode((String) row[2]);
            dto.setEocName((String) row[3]);
            dto.setUocId((String) row[4]);
            dto.setPcs(pcMap.getOrDefault(dto.getEocId(), new ArrayList<>()));
            return dto;
        }).collect(Collectors.groupingBy(EocDTO::getUocId));

        return uocRows.stream().map(row -> {
            UocDTO dto = new UocDTO();
            dto.setUocId((String) row[1]);
            dto.setUocCode((String) row[2]);
            dto.setUocName((String) row[3]);
            dto.setEocs(eocMap.getOrDefault(dto.getUocId(), new ArrayList<>()));
            return dto;
        }).collect(Collectors.toList());
    }
}
