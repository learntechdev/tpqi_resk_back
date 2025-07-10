package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.DTO.EocDTO;
import com.TPQI.thai2learn.DTO.PcDTO;
import com.TPQI.thai2learn.DTO.UocDTO;

import java.util.List;

public interface CompetencyRepository {
    Long findOccLevelIdByExamScheduleId(String examScheduleId);

    List<UocDTO> findUocsByOccLevelId(Long occLevelId);

    List<EocDTO> findEocsByUocIds(List<String> uocIds);

    List<PcDTO> findPcsByEocIds(List<String> eocIds);
}