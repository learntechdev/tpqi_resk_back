package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.ReskCertificateTypeDTO;
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskCertificateTypeRepository;
import org.springframework.data.domain.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MasterDataCertifiteTypeServiceImpl implements MasterDataCertifiteTypeService {

    @Autowired
    private ReskCertificateTypeRepository repository;

    @Override
    public List<ReskCertificateTypeDTO> getAllCertificateTypes() {
        return repository.findAll(Sort.by("id").ascending())
        .stream()
        .map(e -> new ReskCertificateTypeDTO(e.getId(), e.getNameTh()))
        .collect(Collectors.toList());
    }
}
