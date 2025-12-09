package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.EocDTO;
import com.TPQI.thai2learn.DTO.PcDTO;
import com.TPQI.thai2learn.DTO.RelatedQualificationDTO;
import com.TPQI.thai2learn.DTO.UocDTO;

import java.util.List;

public interface CompetencyRepository {
    Long findOccLevelIdByExamScheduleId(String examScheduleId);

    List<UocDTO> findUocsByOccLevelId(Long occLevelId);

    List<EocDTO> findEocsByUocIds(List<String> uocIds);

    List<PcDTO> findPcsByEocIds(List<String> eocIds);

    String findTier2TitleByOccLevelId(Long occLevelId);

    List<RelatedQualificationDTO> findRelatedQualificationsByTier2Title(String tier2Title, Long excludeId);

    Long findExamScheduleIdByTpqiExamNo(String tpqiExamNo);
}