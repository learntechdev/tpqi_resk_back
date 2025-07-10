package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.EocDTO;
import com.TPQI.thai2learn.DTO.PcDTO;
import com.TPQI.thai2learn.DTO.RelatedQualificationDTO;
import com.TPQI.thai2learn.DTO.UocDTO;
import com.TPQI.thai2learn.repositories.tpqi_asm.CompetencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompetencyService {

    @Autowired
    private CompetencyRepository competencyRepository;

    @Transactional(readOnly = true)
    public List<UocDTO> getCompetencyTreeByExamScheduleId(String examScheduleId) {

        Long occLevelId = competencyRepository.findOccLevelIdByExamScheduleId(examScheduleId);
        if (occLevelId == null) {
            return new ArrayList<>();
        }

        List<UocDTO> uocList = competencyRepository.findUocsByOccLevelId(occLevelId);
        if (uocList.isEmpty()) {
            return uocList;
        }

        Map<String, UocDTO> uocMap = uocList.stream()
                .collect(Collectors.toMap(UocDTO::getUocId, uoc -> uoc, (existing, replacement) -> existing));
        List<String> uocIds = new ArrayList<>(uocMap.keySet());


        List<EocDTO> eocList = competencyRepository.findEocsByUocIds(uocIds);
        if (eocList.isEmpty()) {
            return uocList;
        }
        
        Map<String, EocDTO> eocMap = eocList.stream()
                .collect(Collectors.toMap(EocDTO::getEocId, eoc -> eoc, (existing, replacement) -> existing));
        List<String> eocIds = new ArrayList<>(eocMap.keySet());


        List<PcDTO> pcList = competencyRepository.findPcsByEocIds(eocIds);
        
        Map<String, PcDTO> pcMap = pcList.stream()
                .collect(Collectors.toMap(PcDTO::getPcId, pc -> pc, (existing, replacement) -> existing));
        
        for (PcDTO pc : pcMap.values()) {
            EocDTO parentEoc = eocMap.get(pc.getParentEocId());
            if (parentEoc != null) {
                parentEoc.getPerformanceCriteria().add(pc);
            }
        }

        for (EocDTO eoc : eocMap.values()) {
            UocDTO parentUoc = uocMap.get(eoc.getParentUocId());
            if (parentUoc != null) {
                parentUoc.getElementsOfCompetency().add(eoc);
            }
        }
        
        return new ArrayList<>(uocMap.values());
    }



    @Transactional(readOnly = true)
    public List<RelatedQualificationDTO> getRelatedQualifications(String examScheduleId) {
        Long occLevelId = competencyRepository.findOccLevelIdByExamScheduleId(examScheduleId);
        if (occLevelId == null) {
            return new ArrayList<>();
        }

        String tier2Title = competencyRepository.findTier2TitleByOccLevelId(occLevelId);

        if (tier2Title == null || tier2Title.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return competencyRepository.findRelatedQualificationsByTier2Title(tier2Title);
    }
}