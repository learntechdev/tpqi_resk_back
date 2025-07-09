package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.entities.tpqi_asm.ReskSettingsFileExtension;
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskSettingsFileExtensionRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReskSettingsFileExtensionServiceImpl implements ReskSettingsFileExtensionService {

    private final ReskSettingsFileExtensionRepository repository;

    public ReskSettingsFileExtensionServiceImpl(ReskSettingsFileExtensionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ReskSettingsFileExtension> getAllFileExtensions() {
        return repository.findAll();
    }

    @Override
    public Optional<ReskSettingsFileExtension> getFileExtensionById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public ReskSettingsFileExtension updateFileExtension(ReskSettingsFileExtension updatedFileExt) {
        return repository.save(updatedFileExt);
    }

    @Override
    public void deleteFileExtension(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
