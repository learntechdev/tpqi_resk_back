package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.ReskCertificateTypeDTO;
import com.TPQI.thai2learn.DTO.UocDTO;
import com.TPQI.thai2learn.services.MasterDataCertifiteTypeService;
import com.TPQI.thai2learn.services.UocHierarchyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Masterdata")
public class MasterDataController {

    private final MasterDataCertifiteTypeService masterDataService;
    private final UocHierarchyService uocHierarchyService;

    public MasterDataController(
            MasterDataCertifiteTypeService masterDataService,
            UocHierarchyService uocHierarchyService) {
        this.masterDataService = masterDataService;
        this.uocHierarchyService = uocHierarchyService;
    }

    @GetMapping("/ListCertificateTypes")
    public List<ReskCertificateTypeDTO> getCertificateTypes() {
        return masterDataService.getAllCertificateTypes();
    }

    @GetMapping("/UocSelectorHierarchy/{occLevelId}")
    public ResponseEntity<List<UocDTO>> getHierarchy(@PathVariable String occLevelId) {
        return ResponseEntity.ok(uocHierarchyService.getHierarchyByOccLevelId(occLevelId));
    }
}
