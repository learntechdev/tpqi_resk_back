package com.TPQI.thai2learn.services;

import java.util.List;
import java.util.Optional;

import com.TPQI.thai2learn.entities.tpqi_asm.ReskSettingsFileExtension;

public interface ReskSettingsFileExtensionService {

    List<ReskSettingsFileExtension> getAllFileExtensions();

    Optional<ReskSettingsFileExtension> getFileExtensionById(Integer id);

    ReskSettingsFileExtension updateFileExtension(ReskSettingsFileExtension updatedFileExt);

    void deleteFileExtension(Integer id);

    boolean existsById(Integer id);
}
