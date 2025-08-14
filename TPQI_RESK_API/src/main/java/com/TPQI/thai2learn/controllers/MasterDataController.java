package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.ReskCertificateTypeDTO;
import com.TPQI.thai2learn.services.MasterDataCertifiteTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import com.TPQI.thai2learn.DTO.StatusDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.types.AssessmentStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/Masterdata")
public class MasterDataController {

    @Autowired
    private MasterDataCertifiteTypeService masterDataService;

    @GetMapping("/ListCertificateTypes")
    public List<ReskCertificateTypeDTO> getCertificateTypes() {
        return masterDataService.getAllCertificateTypes();
    }

    @GetMapping("/assessment-statuses")
    public List<StatusDTO> getAssessmentStatuses() {
        return AssessmentStatus.getDistinctStatusDTOs();
    }
}
