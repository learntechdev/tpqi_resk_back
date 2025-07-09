package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.entities.ext_data.CpApplication;

import java.util.List;

public interface CpApplicationService {
    List<CpApplication> getAllApplications();
    List<CpApplication> getApplicationsByPersonId(int personId);
}
