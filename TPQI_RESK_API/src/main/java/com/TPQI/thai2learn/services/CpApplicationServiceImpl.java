package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.entities.ext_data.CpApplication;
import com.TPQI.thai2learn.repositories.ext_data.CpApplicationRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CpApplicationServiceImpl implements CpApplicationService {

    private final CpApplicationRepository repository;

    public CpApplicationServiceImpl(CpApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CpApplication> getAllApplications() {
        return repository.findAll();
    }

    @Override
    public List<CpApplication> getApplicationsByPersonId(int personId) {
        return repository.findByPersonId(personId);
    }
}
