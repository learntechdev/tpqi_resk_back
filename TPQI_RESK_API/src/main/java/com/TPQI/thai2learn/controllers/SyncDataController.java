package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.DTO.TPQIProfessionDTO;
import com.TPQI.thai2learn.services.TPQIProfessionService;
import com.TPQI.thai2learn.services.TPQIProfessionSyncService;

import com.TPQI.thai2learn.entities.ext_data.CpApplication;
import com.TPQI.thai2learn.services.CpApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/SyncData")
public class SyncDataController {

    @Autowired
    private TPQIProfessionService tpqiProfessionService;

    @GetMapping("/ShowAllTPQIProfession")
    public List<TPQIProfessionDTO> getTpqiProfessionSync() {
        return tpqiProfessionService.getTpqiProfessionData();
    }
    
    @Autowired
    private TPQIProfessionSyncService syncService;
    @GetMapping("/TpqiNet_tpqi_profession")
    public String syncTPQIProfessions() {
        syncService.syncTPQIProfessions();
        return "Sync completed.";
    }

    private final CpApplicationService service;

    public SyncDataController(CpApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<CpApplication> getApplications(@RequestParam(required = false) Integer personId) {
        if (personId != null) {
            return service.getApplicationsByPersonId(personId);
        }
        return service.getAllApplications();
    }
}  
